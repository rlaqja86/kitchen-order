package cloudkitchen.order.dispatcher;

import cloudkitchen.courier.consumer.CourierMatchStrategy;

public interface CourierDispatcher {
     void dispatch(CourierMatchStrategy strategy);
}
