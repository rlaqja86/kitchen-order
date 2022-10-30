package cloudkitchen.order.calculation;

import cloudkitchen.order.model.DeliveryTask;
import cloudkitchen.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderTimeCalculator {
    public final DeliveryTaskService deliveryTaskService;

    public OrderTimeCalculator(DeliveryTaskService deliveryTaskService) {
        this.deliveryTaskService = deliveryTaskService;
    }

    public AverageCalculationResponse calculate() {
        Set<DeliveryTask> deliveryTasks = deliveryTaskService.getAllDeliveryTasks();

        Double averageFoodWaitTime = deliveryTasks.stream()
                .map(R -> DateUtils.getDifference( R.getOrderReadyTime(), R.getCourierPickedUpTime()))
                .mapToInt(Integer::intValue)
                .average().orElse(0L);

        Double averageCourierWaitTime = deliveryTasks.stream()
                .map(R -> DateUtils.getDifference( R.getCourierArrivalTime(), R.getCourierPickedUpTime()))
                .mapToInt(Integer::intValue)
                .average().orElse(0L);

        return AverageCalculationResponse.builder()
                .averageCourierWaitTime(averageCourierWaitTime)
                .averageFoodWaitTime(averageFoodWaitTime)
                .build();
    }

}
