package cloudkitchen.order.api;

import cloudkitchen.order.model.Order;
import cloudkitchen.order.publisher.OrderProducer;
import cloudkitchen.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class OrderGenerator {

    private final OrderService orderService;
    private final OrderProducer orderProducer;

    public OrderGenerator(OrderService orderService, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.orderProducer = orderProducer;
    }

    public void generate() {
        try {

            List<Order> orderList = orderService.getAllOrders();

            orderList.parallelStream().forEachOrdered(order -> {
                try {
                    orderProducer.produce(order);
                    //to only accept 2 orders per second
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            log.error("[OrderQueueProducer] failed to produce order due to error", e);
        }
    }
}
