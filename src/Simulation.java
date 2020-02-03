import java.util.Random;

/**
 * Represents and runs an event-based bank simulation
 */
public class Simulation {

    // current time of day
    private int clock;
    
    private int numTellers;
    private int timeLimit;
    private int arrivalTimeMean;
    private int arrivalTimeVariance;
    private int serviceTimeMean;
    private int serviceTimeVariance;
    private int customerCount;
    private int customerArrivalsCount;
    private int maximumCustomerWaitTime;
    private int totalWaitTime;
    private int totalInterArrivalTime;
    private int totalServiceTime;

    // current list of events to be processed
    private EventPriorityQueue eventPriorityQueue;

    // represents customers in line for a teller
    private EventQueue[] tellerQueues;

    /**
     * Creates a simulation instance
     * @param numTellers the number of tellers
     * @param timeLimit the time limit of the simulation
     */
    public Simulation(int numTellers, int arrivalTimeMean, int arrivalTimeVariance, int serviceTimeMean, int serviceTimeVariance, int timeLimit) {
        this.numTellers = numTellers;
        this.arrivalTimeMean = arrivalTimeMean;
        this.arrivalTimeVariance = arrivalTimeVariance;
        this.serviceTimeMean = serviceTimeMean;
        this.serviceTimeVariance = serviceTimeVariance;
        this.timeLimit = timeLimit;
    }

    /**
     * Runs the simulation
     */
    public void run() {
        initializeVariables();
        createArrivalEvent();
        while (clock <= timeLimit) {
            // get event from event queue
            EventItem eventItem = eventPriorityQueue.removeItem();
            updateIdleTimes(eventItem.getTimeOfDay() - clock);
            clock = eventItem.getTimeOfDay();

            // if arrival
            if (eventItem.getTypeOfEvent() == -1) {
                //place customer into shortest queue;
                int shortestTellerQueueIndex = getShortestTellerQueueIndex();
                EventQueue shortestTellerQueue = tellerQueues[shortestTellerQueueIndex];

                // add event to teller queue
                shortestTellerQueue.addItem(eventItem);

                // if only event in teller queue, create departure event and new arrival event
                if (shortestTellerQueue.getCurrentNumber() == 1) {
                    createDepartureEvent(eventItem, shortestTellerQueueIndex);
                }
                createArrivalEvent();
            // if departure event
            } else {
                customerCount++;
                // determine which teller queue event is in
                int queueIndex = eventItem.getTypeOfEvent();
                EventQueue tellerQueue = tellerQueues[queueIndex];

                // calculate wait time of customer
                int waitTime = clock - tellerQueue.removeItem().getTimeOfDay() - eventItem.getServiceTime();
                updateMaximumCustomerWaitTime(waitTime);
                totalWaitTime += waitTime;

                // if teller queue is empty, create departure event for next event in the queue
                if (!tellerQueue.isEmpty()) {
                    // create departure event
                    EventItem nextEventItem = tellerQueue.getItem();
                    createDepartureEvent(nextEventItem, queueIndex);
                }
            }

            if (clock % 500 == 0) {
                printSnapshot();
            }
        }
        printStatistics();
    }

    /**
     * Prints a snapshot of various statistics
     */
    private void printSnapshot() {
        System.out.println("Clock Time: " + clock);
        for (int i = 0; i < numTellers; i++) {
            System.out.println("Number of customers for Teller (" + i + "): " + tellerQueues[i].getCurrentNumber());
        }
        System.out.println("Number of items in event queue: " + eventPriorityQueue.getSize());
        System.out.println();
    }

    /**
     * Creates an arrival event and adds it to the priority queue
     */
    private void createArrivalEvent() {
        int arrivalTime = uniformRandom(arrivalTimeMean, arrivalTimeVariance, new Random());
        int serviceTime = uniformRandom(serviceTimeMean, serviceTimeVariance, new Random());
        EventItem eventItem = new EventItem(clock + arrivalTime, serviceTime, -1);
        eventPriorityQueue.addItem(eventItem);
        totalInterArrivalTime += arrivalTime;
        totalServiceTime += serviceTime;
        customerArrivalsCount++;
    }

    /**
     * Creates a departure event and adds it to the priority queue
     * @param eventItem the current event item
     * @param typeOfEvent the index of the current teller queue
     */
    private void createDepartureEvent(EventItem eventItem, int typeOfEvent) {
        EventItem departureEventItem = new EventItem(clock + eventItem.getServiceTime(), eventItem.getServiceTime(), typeOfEvent);
        eventPriorityQueue.addItem(departureEventItem);
    }

    /**
     * Prepares the simulation
     */
    private void initializeVariables() {
        clock = 0;
        customerCount = 0;
        customerArrivalsCount = 0;
        maximumCustomerWaitTime = 0;
        totalWaitTime = 0;
        eventPriorityQueue = new EventPriorityQueue();
        createTellersQueues();
    }

    /**
     * Creates teller queues
     */
    private void createTellersQueues() {
        tellerQueues = new EventQueue[numTellers];
        for (int i = 0; i < numTellers; i++) {
            tellerQueues[i] = new EventQueue();
        }
    }

    /**
     * updates the idle times for each teller
     * @param idleTime the current idle time
     */
    private void updateIdleTimes(int idleTime) {
        for (int i = 0; i < tellerQueues.length; i++) {
            if (tellerQueues[i].isEmpty()) {
                tellerQueues[i].incrementTotalIdleTime(idleTime);
            }
        }
    }

    /**
     * updates the maximum customer wait time, if necessary
     * @param waitTime a possible wait time
     */
    private void updateMaximumCustomerWaitTime(int waitTime) {
        if (waitTime > maximumCustomerWaitTime) {
            maximumCustomerWaitTime = waitTime;
        }
    }

    /**
     * Returns the index of the shortes teller queue
     * @return the index of the shortest teller queue
     */
    private int getShortestTellerQueueIndex() {
        int shortest = Integer.MAX_VALUE;
        int shortestTellerQueueIndex = -1;
        for (int i = 0; i < tellerQueues.length; i++) {
            int currentNumber = tellerQueues[i].getCurrentNumber();
            if (currentNumber < shortest) {
                shortest = currentNumber;
                shortestTellerQueueIndex = i;
            }
        }
        return shortestTellerQueueIndex;
    }

    /**
     * Generates a uniform random number in the range mean +- variant
     * @param mean mean of range
     * @param variant variant of range
     * @param rand Random instance
     * @return a uniform random number in the range mean +- variant
     */
    private int uniformRandom(int mean, int variant, Random rand) {
        int small = mean - variant;
        int range = 2 * variant + 1;
        return small + rand.nextInt(range);
    }

    /**
     * prints various statistics about the simulation
     */
    private void printStatistics() {
        System.out.println("The total number of customers processed: " + customerCount);
        System.out.println("The average inter-arrival time: " + totalInterArrivalTime / customerArrivalsCount);
        System.out.println("The average service time: " + totalServiceTime / customerArrivalsCount);
        System.out.println("The average wait time per customer: " + totalWaitTime / customerCount);
        printTellerQueuesIdlePercents();
        System.out.println("The maximum customer wait time: " + maximumCustomerWaitTime);
        System.out.println("The maximum queue length of any customer queue: " + getMaxTellerQueueLength());
        System.out.println("The total number of people left in queues at the end of the simulation: " + getNumCustomersLeftOver());
    }

    /**
     * Gets the maximum teller queue length
     * @return the maximum teller queue length
     */
    private int getMaxTellerQueueLength() {
        int maxTellerQueueLength = 0;
        for (int i = 0; i < numTellers; i++) {
            int tellerQueueLength = tellerQueues[i].getMaxLength();
            if (tellerQueueLength > maxTellerQueueLength) {
                maxTellerQueueLength = tellerQueueLength;
            }
        }
        return maxTellerQueueLength;
    }

    /**
     * Gets the number of customers left over
     * @return the number of customers left over
     */
    private int getNumCustomersLeftOver() {
        int numCustomersLeftOver = 0;
        for (int i = 0; i < numTellers; i++) {
            numCustomersLeftOver += tellerQueues[i].getCurrentNumber();
        }
        return numCustomersLeftOver;
    }

    /**
     * Prints the percent of time each teller spend idle
     */
    private void printTellerQueuesIdlePercents() {
        for (int i = 0; i < numTellers; i++) {
            EventQueue tellerQueue = tellerQueues[i];
            System.out.println("Percent of idle time for teller (" + i + "): " + (int)((double)tellerQueue.getTotalIdleTime() / clock * 100) + "%");
        }
    }
}