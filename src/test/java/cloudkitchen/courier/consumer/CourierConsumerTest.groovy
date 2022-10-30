package cloudkitchen.courier.consumer

import cloudkitchen.courier.model.Courier
import cloudkitchen.order.calculation.DeliveryTaskService
import cloudkitchen.order.model.Order
import cloudkitchen.order.queue.CourierQueue
import spock.lang.Specification

class CourierConsumerTest extends Specification {
    CourierQueue queue = Mock()
    Order order = new Order()
    DeliveryTaskService deliveryTaskService = new DeliveryTaskService()
    Courier courier = new Courier()
    List<String> matchedOrders = new ArrayList<>()


    def "courier consumer FIFO strategy test" () {
        given:
        order.setId("1")
        order.setName("ordername")
        order.setPrepTime(1)
        CourierConsumer consumer =
                new CourierConsumer(CourierMatchStrategy.FIFO, order, deliveryTaskService, queue)
        when:
        queue.take() >> courier
        consumer.run()
        then:
        deliveryTaskService.getAllDeliveryTasks().size() == 1
        deliveryTaskService.getAllDeliveryTasks().head().getOrder().getId() == "1"
        deliveryTaskService.getAllDeliveryTasks().head().getOrder().getName() == "ordername"
        deliveryTaskService.getAllDeliveryTasks().head().getCourierArrivalTime().getTime() > deliveryTaskService.getAllDeliveryTasks().head().getOrderReadyTime().getTime()
        deliveryTaskService.getAllDeliveryTasks().head().getCourierPickedUpTime().getTime() > deliveryTaskService.getAllDeliveryTasks().head().getOrderReadyTime().getTime()
        deliveryTaskService.getAllDeliveryTasks().head().getCourierPickedUpTime().getTime() >= deliveryTaskService.getAllDeliveryTasks().head().getCourierArrivalTime().getTime()

    }

    def "courier consumer Match strategy test" () {
        given:
        order.setId("1")
        order.setName("ordername")
        order.setPrepTime(1)
        matchedOrders.add("1")
        courier.setMatchedOrders(matchedOrders)

        CourierConsumer consumer =
                new CourierConsumer(CourierMatchStrategy.MATCH, order, deliveryTaskService, queue)

        queue.take() >> courier
        when:
        consumer.run()
        then:
        deliveryTaskService.getAllDeliveryTasks().size() == 1
        deliveryTaskService.getAllDeliveryTasks().head().getOrder().getId() == "1"
        deliveryTaskService.getAllDeliveryTasks().head().getCourierArrivalTime().getTime() > deliveryTaskService.getAllDeliveryTasks().head().getOrderReadyTime().getTime()
        deliveryTaskService.getAllDeliveryTasks().head().getCourierPickedUpTime().getTime() > deliveryTaskService.getAllDeliveryTasks().head().getOrderReadyTime().getTime()
        deliveryTaskService.getAllDeliveryTasks().head().getCourierPickedUpTime().getTime() >= deliveryTaskService.getAllDeliveryTasks().head().getCourierArrivalTime().getTime()

        1 * queue.add(courier)
    }
}
