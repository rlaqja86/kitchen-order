package cloudkitchen.order.queue;

import cloudkitchen.order.model.Order;

import java.util.concurrent.BlockingQueue;

public interface OrderBlockingQueue<T> {
    public BlockingQueue<T> getQueue();
    public void add(T data);
    public T take();
}
