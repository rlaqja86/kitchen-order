package cloudkitchen.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Setter
@Getter
public class DeliveryTask {
    private Order order;
    private Date orderReadyTime;
    private Date courierArrivalTime;
    private Date courierPickedUpTime;
}
