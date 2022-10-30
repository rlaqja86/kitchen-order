package cloudkitchen.order.service;

import cloudkitchen.order.model.Order;
import cloudkitchen.order.publisher.OrderProducer;
import cloudkitchen.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
public class OrderService {

    private final OrderProducer orderProducer;
    private final OrderRepository orderRepository;

    public OrderService(OrderProducer orderProducer, OrderRepository orderRepository) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() throws IOException {
        return orderRepository.getAllOrders();
    }


}
