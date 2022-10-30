package cloudkitchen.order.dispatcher;

import cloudkitchen.courier.consumer.CourierConsumer;
import cloudkitchen.courier.consumer.CourierMatchStrategy;
import cloudkitchen.order.model.Order;
import cloudkitchen.order.queue.CourierQueue;
import cloudkitchen.order.queue.OrderQueue;
import cloudkitchen.order.calculation.DeliveryTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class CourierMatchDispatcher implements CourierDispatcher{

    private final OrderQueue orderQueue;
    private final CourierQueue courierQueue;
    private final ExecutorService executorService;
    private final DeliveryTaskService collector;

    public CourierMatchDispatcher(OrderQueue orderQueue, CourierQueue courierQueue, DeliveryTaskService collector) {
        this.orderQueue = orderQueue;
        this.executorService = Executors.newCachedThreadPool();
        this.collector = collector;
        this.courierQueue = courierQueue;
    }

    @Override
    public void dispatch(CourierMatchStrategy courierMatchStrategy) {
      consume(courierMatchStrategy);
    }

    private void consume(CourierMatchStrategy courierMatchStrategy) {
        while(true) {
            try {
                Order order = orderQueue.take();
                executorService.submit(new CourierConsumer(courierMatchStrategy, order, collector, courierQueue));
            } catch (Exception e) {
                log.error("Failed to dispatch courier due to error : ", e);
            }
        }
    }
}
