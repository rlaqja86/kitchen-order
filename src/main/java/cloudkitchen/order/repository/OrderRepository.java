package cloudkitchen.order.repository;

import cloudkitchen.order.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderRepository {

    public List<Order> getAllOrders() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String path = getClass().getResource("/data/dispatch_orders.json").getFile();

        return  Arrays.asList(objectMapper.readValue(new File(path), Order[].class));
    }
}
