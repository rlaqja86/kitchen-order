package cloudkitchen.courier.repository;

import cloudkitchen.courier.model.Courier;
import cloudkitchen.order.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CourierRepository {
    public List<Courier> getAllCouriers() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String path = getClass().getResource("/data/couriers.json").getFile();

        return  Arrays.asList(objectMapper.readValue(new File(path), Courier[].class));
    }
}
