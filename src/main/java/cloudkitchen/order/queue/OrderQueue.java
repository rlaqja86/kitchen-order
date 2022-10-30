package cloudkitchen.order.queue;

import cloudkitchen.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Component
@Slf4j
public class OrderQueue implements OrderBlockingQueue<Order> {
    private final BlockingQueue<Order> queue;

    public OrderQueue() {
        queue = new LinkedBlockingDeque<>();
    }
    @Override
    public BlockingQueue<Order> getQueue () {
        return queue;
    }

    @Override
    public void add(Order order) {
        queue.add(order);
    }
    @Override
    public Order take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            log.error("failed take from queue!", e);
        }
        return null;
    }
}
