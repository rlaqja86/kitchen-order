package cloudkitchen.order.calculation

import cloudkitchen.order.model.DeliveryTask
import spock.lang.Specification

class OrderTimeCalculatorTest extends Specification {
    OrderTimeCalculator sut
    DeliveryTaskService service = Mock()
    Set<DeliveryTask> deliveryTasks

    def setup() {
        sut = new OrderTimeCalculator(service)
        String task1orderReadyTime = 'October 29, 2022 12:00:00';
        String task1ArrivalTime = 'October 29, 2022 12:00:05';
        String task1PickedUpTime = 'October 29, 2022 12:00:10';

        String task2orderReadyTime = 'October 29, 2022 12:00:20';
        String task2ArrivalTime = 'October 29, 2022 12:00:25';
        String task2PickedUpTime = 'October 29, 2022 12:00:30';

        String task3orderReadyTime = 'October 29, 2022 12:00:40';
        String task3ArrivalTime = 'October 29, 2022 12:00:45';
        String task3PickedUpTime = 'October 29, 2022 12:00:50';

        deliveryTasks = new HashSet<>()

        deliveryTasks.add(DeliveryTask.builder()
                .courierArrivalTime(new Date(task1ArrivalTime))
                .orderReadyTime(new Date(task1orderReadyTime))
                .courierPickedUpTime(new Date(task1PickedUpTime)).build())
        deliveryTasks.add(DeliveryTask.builder()
                .courierArrivalTime(new Date(task2ArrivalTime))
                .orderReadyTime(new Date(task2orderReadyTime))
                .courierPickedUpTime(new Date(task2PickedUpTime)).build())
        deliveryTasks.add(DeliveryTask.builder()
                .courierArrivalTime(new Date(task3ArrivalTime))
                .orderReadyTime(new Date(task3orderReadyTime))
                .courierPickedUpTime(new Date(task3PickedUpTime)).build())
    }

    def "calculate average courier wait time and food wait time"() {
        given:
        service.getAllDeliveryTasks() >> deliveryTasks
        when:
        AverageCalculationResponse response = sut.calculate()
        then:
        response != null
        response.getAverageCourierWaitTime() == 5
        response.getAverageFoodWaitTime() == 10
    }
}
