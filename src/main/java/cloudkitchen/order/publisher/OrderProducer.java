package cloudkitchen.order.publisher;

import cloudkitchen.order.model.Order;
import cloudkitchen.order.queue.OrderQueue;
import cloudkitchen.util.LogGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class OrderProducer {

    private final OrderQueue orderQueue;

    public OrderProducer(OrderQueue orderQueue) {
        this.orderQueue = orderQueue;
    }


    public void produce(Order data) {
        try {
            LogGenerator.generateLog("ORDER DISPATCH : ", data.getId(), data.getName(), new Date(System.currentTimeMillis()));
            orderQueue.add(data);
        } catch (Exception exception) {
            log.error("Failed to publish order :" ,exception);
        }
    }

}
