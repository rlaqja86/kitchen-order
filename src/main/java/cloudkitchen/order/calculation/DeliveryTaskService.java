package cloudkitchen.order.calculation;

import cloudkitchen.order.model.DeliveryTask;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DeliveryTaskService {
    private Set<DeliveryTask> deliveryTasks;

    public DeliveryTaskService() {
        deliveryTasks = new HashSet<>();
    }

    public  Set<DeliveryTask> getAllDeliveryTasks() {
        return deliveryTasks;
    }

    public void saveDeliveryTask(DeliveryTask deliveryTask) {
        deliveryTasks.add(deliveryTask);
    }
}
