# Real time order - assign flow

### Basic Environment
~~~~
IDE - Intellij idea

language - JAVA

framework - springboot

jdk version - Jdk11.0.3

build tool - gradle
~~~~

### How to run
~~~~
in intellij IDEA

1.GO to Application class

2.right click main method. and click run Application.main

or

go to edit configuration

set main class as Application class

click OK and run application configuration
~~~~

### How to evaluate average wait time
~~~~
when application started. below console input command text will be shown

[input : 1 -> RUN FIFO courier match, 2 -> RUN Specific courier match, q - > enter exit program

input 1 -> enter will run  FIFO strategy

input 2 -> enter will run MATCHED strategy

input 3 -> enter will exit program

default test courier number is 30 and each has 4 matched order
~~~~

### Architecture & Design pattern
~~~~
Produce Consumer pattern

1. FIFO Strategy

the order producer produce 2 orders for every 1 sconds and put order to order blocking queue
after that order consumer take order from order queue and try to take courier from courier blocking queue
if there is available courier exist in courier queue, calculate random courier dispatch time.

and thread will sleep if there is remain order preperation time or courier arrival time.

after wake up. order is picked by courier

2. MATCHED Strategy

the order produce flow is same with FIFO strategy. but in this case. courier has 4 matched order. 
when courier consumer take courier from courier queue. it checks courier has matched order. if courier has matched order,
flow is same with FIFO strategy. but if not. courier will get back to courier queue and repeat it until fit courier is
matched
~~~~