package cloudkitchen.courier.consumer;

import cloudkitchen.courier.model.Courier;
import cloudkitchen.courier.service.CourierService;
import cloudkitchen.order.model.Order;
import cloudkitchen.order.queue.CourierQueue;
import cloudkitchen.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CourierInitializer {
    
    private final CourierQueue courierQueue;
    private final CourierService courierService;
    private final OrderService orderService;

    private final Integer MAX_COURIER_ORDER_COUNT = 4;

    public CourierInitializer(CourierQueue courierQueue, CourierService courierService, OrderService orderService) {
        this.courierService = courierService;
        this.courierQueue = courierQueue;
        this.orderService = orderService;
    }

    public void initCouriers() {
        try {
            List<Courier> couriers = addMatchedOrdersToCouriers(courierService.getAllCouriers());
            couriers.forEach(courierQueue::add);
        } catch (Exception exception) {
            log.error("[CourierInitializer] failed to initialize courier queue");
        }
    }

    private List<Courier> addMatchedOrdersToCouriers(List<Courier> couriers) {
        try {
            List<Order> orders = orderService.getAllOrders();
            int orderIndex = 0;
            for (Courier courier : couriers) {
                List<String> matchedOrders = new ArrayList<>();
                for (int i = orderIndex; i < orderIndex + MAX_COURIER_ORDER_COUNT; i++) {
                    matchedOrders.add(orders.get(i).getId());
                }
                courier.setMatchedOrders(matchedOrders);
                orderIndex += MAX_COURIER_ORDER_COUNT;
            }

        } catch (Exception e) {
            log.error("[CourierInitializer] addMatchedOrderToCourier failed to match orders to couriers");
        }
        return couriers;
    }
}
