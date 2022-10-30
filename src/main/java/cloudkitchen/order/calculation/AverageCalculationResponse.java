package cloudkitchen.order.calculation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AverageCalculationResponse {
    private Double averageFoodWaitTime;
    private Double averageCourierWaitTime;
}
