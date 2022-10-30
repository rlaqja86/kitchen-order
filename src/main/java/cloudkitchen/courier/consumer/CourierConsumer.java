package cloudkitchen.courier.consumer;

import cloudkitchen.courier.model.Courier;
import cloudkitchen.order.model.DeliveryTask;
import cloudkitchen.order.model.Order;
import cloudkitchen.order.calculation.DeliveryTaskService;
import cloudkitchen.order.queue.CourierQueue;
import cloudkitchen.util.DateUtils;
import cloudkitchen.util.LogGenerator;
import cloudkitchen.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static cloudkitchen.constraint.Constraint.MAX_TIME;
import static cloudkitchen.constraint.Constraint.MIN_TIME;

@Slf4j
public class CourierConsumer implements Runnable {

    private final Order order;
    private final DeliveryTaskService deliveryTaskService;
    private final CourierQueue courierQueue;
    private final CourierMatchStrategy courierMatchStrategy;

    public CourierConsumer(CourierMatchStrategy courierMatchStrategy, Order order, DeliveryTaskService deliveryTaskService, CourierQueue courierQueue) {
        this.order = order;
        this.deliveryTaskService = deliveryTaskService;
        this.courierQueue = courierQueue;
        this.courierMatchStrategy = courierMatchStrategy;
    }

    @Override
    public void run() {
        try {
            Date now = new Date(System.currentTimeMillis());
            //dispatch couriers by strategy
            if (courierMatchStrategy == CourierMatchStrategy.FIFO) {
                runWithFiFoStrategy(now);
            } else if (courierMatchStrategy == CourierMatchStrategy.MATCH) {
                runWithMatchStrategy(now);
            }
        }

        catch (InterruptedException e) {
           log.error("[CourierConsumer] InterruptedException occurred during consume orders, - OrderId : {} ", order.getId(), e);
        }
    }

    private void runWithMatchStrategy(Date now) throws InterruptedException {
        while (true) {
            Courier courier = courierQueue.take();
           if (courier.getMatchedOrders().contains(order.getId())) {
               processOrder(now, courier);
               break;
           } else {
               courierQueue.add(courier);
           }
        }
    }

    public void runWithFiFoStrategy(Date orderStarted) throws InterruptedException {
        Courier courier = courierQueue.take();
        processOrder(orderStarted, courier);
    }

    private void processOrder(Date orderStarted, Courier courier) throws InterruptedException {
        Date now = new Date(System.currentTimeMillis());
        LogGenerator.generateLog("COURIER WAIT : ", order.getId(), order.getName(), now);

        int dispatchedTime = DateUtils.getDifference(orderStarted, now) * 1000;
        int courierArrivalTime = RandomUtils.generate(MIN_TIME, MAX_TIME) * 1000;
        int orderPrepTime = order.getPrepTime() * 1000;

        // if dispatched time is longer than orderPrepTime then orderPrepTime is ignored because order is already prepared during dispatch time
        // in this case, thread will sleep only for courier arrival time after dispatched
        // if dispatched time is shorter than orderPrepTime. then must compare between remain order preperation time and courier arrival time
        int totalWaitTime = dispatchedTime > orderPrepTime ? courierArrivalTime : Math.max ((orderPrepTime - dispatchedTime) , courierArrivalTime);

        //wait until meal preparation time is finished or courier arrived
        Thread.sleep(totalWaitTime);
        LogGenerator.generateLog("COURIER PICKED UP : ", order.getId(), order.getName(), DateUtils.addMilliseconds(now, totalWaitTime));

        deliveryTaskService.saveDeliveryTask(DeliveryTask.builder()
                .order(order)
                .orderReadyTime(DateUtils.addMilliseconds(orderStarted, orderPrepTime)) //order started time after order preperation time
                .courierArrivalTime(DateUtils.addMilliseconds(now, courierArrivalTime)) //courier dispatched from queue and after arrived time
                .courierPickedUpTime(DateUtils.addMilliseconds(now, totalWaitTime))
                .build());
        //courier back to idle after delivery
        courierQueue.add(courier);
    }
}
