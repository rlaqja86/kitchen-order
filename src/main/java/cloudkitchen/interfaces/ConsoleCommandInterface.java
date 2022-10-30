package cloudkitchen.interfaces;

import cloudkitchen.courier.consumer.CourierInitializer;
import cloudkitchen.courier.consumer.CourierMatchStrategy;
import cloudkitchen.order.api.OrderGenerator;
import cloudkitchen.order.calculation.AverageCalculationResponse;
import cloudkitchen.order.dispatcher.CourierMatchDispatcher;
import cloudkitchen.order.calculation.OrderTimeCalculator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Component
public class ConsoleCommandInterface {

    private final OrderGenerator orderGenerator;
    private final CourierMatchDispatcher courierMatchDispatcher;
    private final OrderTimeCalculator calculator;
    private final CourierInitializer courierInitializer;

    public ConsoleCommandInterface(OrderGenerator orderGenerator, CourierMatchDispatcher courierMatchDispatcher, OrderTimeCalculator calculator, CourierInitializer courierInitializer) {
        this.orderGenerator = orderGenerator;
        this.courierMatchDispatcher = courierMatchDispatcher;
        this.calculator = calculator;
        this.courierInitializer = courierInitializer;
    }

    @PostConstruct
    public void run() {
        //start consume courier dispatch
        while(true) {
            try {
                operate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void operate() throws InterruptedException {
        courierInitializer.initCouriers();

        while (true) {
            System.out.println("[input : 1 -> RUN FIFO courier match, 2 -> RUN Specific courier match, q - > enter exit program");
            Scanner in = new Scanner(System.in);

            String command = in.next();

            switch(command) {
                case "1" :
                    new Thread(() -> courierMatchDispatcher.dispatch(CourierMatchStrategy.FIFO)).start();
                    System.out.println("Produce Orders with FIFO strategy");
                    orderGenerator.generate();
                    calculateResult();
                    break;
                case "2" :
                    new Thread(() -> courierMatchDispatcher.dispatch(CourierMatchStrategy.MATCH)).start();
                    System.out.println("Produce Orders with Specific Match strategy");
                    orderGenerator.generate();
                    calculateResult();
                    break;
                case "3" :
                    System.out.println("Program exit");
                    System.exit(1);
            }
        }
    }

    private void calculateResult() throws InterruptedException {
        TimeUnit.SECONDS.sleep(40);

        System.out.println("Produce completed");
        AverageCalculationResponse calculationResult = calculator.calculate();
        System.out.println("Average Food wait Time is :" + calculationResult.getAverageFoodWaitTime());
        System.out.println("Average Courier wait Time is :" + calculationResult.getAverageCourierWaitTime());
    }
}
