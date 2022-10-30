package cloudkitchen.order.queue;

import cloudkitchen.courier.model.Courier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
@Component
public class CourierQueue implements OrderBlockingQueue<Courier> {
    private final BlockingQueue<Courier> queue;

    public CourierQueue() {
        queue = new LinkedBlockingDeque<>();
    }

    @Override
    public BlockingQueue<Courier> getQueue () {
        return queue;
    }

    @Override
    public void add(Courier data) {
        queue.add(data);
    }

    @Override
    public Courier take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            log.error("failed take from queue!", e);
        }
        return null;
    }

}
