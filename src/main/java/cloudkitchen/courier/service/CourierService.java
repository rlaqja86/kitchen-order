package cloudkitchen.courier.service;

import cloudkitchen.courier.model.Courier;
import cloudkitchen.courier.repository.CourierRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CourierService {
    private final CourierRepository courierRepository;

    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    public List<Courier> getAllCouriers() throws IOException {
        return courierRepository.getAllCouriers();
    }
}
