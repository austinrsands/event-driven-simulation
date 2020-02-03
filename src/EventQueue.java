import java.util.Queue;
import java.util.LinkedList;

/**
 * Represents a queue for events
 */
public class EventQueue {

    private Queue<EventItem> eventQueue;
    private int currentNumber;
    private int totalIdleTime;
    private int maxLength;

    /**
     * Creats an instance of an event queue
     */
    public EventQueue() {
        currentNumber = 0;
        totalIdleTime = 0;
        maxLength = 0;
        eventQueue = new LinkedList<EventItem>();
    }

    /**
     * Adds an event item to the queue
     * @param eventItem the event item to add
     */
    public void addItem(EventItem eventItem) {
        currentNumber++;
        if (currentNumber > maxLength) {
            maxLength = currentNumber;
        }
        eventQueue.add(eventItem);
    }

    /**
     * Removes an event item from the queue
     * @return the removed event item
     */
    public EventItem removeItem() {
        if (!eventQueue.isEmpty()) {
            currentNumber--;
        }
        return eventQueue.poll();
    }

    /**
     * Gets an event item from the queue
     * @return an event item from the queue
     */
    public EventItem getItem() {
        return eventQueue.peek();
    }

    /**
     * Gets the current number of event items in the queue
     * @return the current number of event items in the queue
     */
    public int getCurrentNumber() {
        return currentNumber;
    }

    /**
     * Gets the total idle time of the teller
     * @return the total idle time of the teller
     */
    public int getTotalIdleTime() {
        return totalIdleTime;
    }

    /**
     * Incremetns the total idle time of the teller
     * @param idleTime the amount to increment by
     */
    public void incrementTotalIdleTime(int idleTime) {
        totalIdleTime += idleTime;
    }

    /**
     * Gets the maximum length of the queue
     * @return the maximum length of the queue
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Determines if queue is empty
     * @return true if queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }
}