import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Represents a priority queue for organizing events
 */
public class EventPriorityQueue {

    private PriorityQueue<EventItem> eventPriorityQueue;

    /**
     * Creates an event priority queue instance
     */
    public EventPriorityQueue() {
        eventPriorityQueue = new PriorityQueue<EventItem>(10, new EventItemSorter());
    }

    /**
     * Adds and item to the queue
     * @param eventItem the event item to add
     */
    public void addItem(EventItem eventItem) {
        eventPriorityQueue.add(eventItem);
    }

    /**
     * Removes an event idem from the queue
     * @return the removed item
     */
    public EventItem removeItem() {
        return eventPriorityQueue.poll();
    }

    /**
     * Return whether the priority queue is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return eventPriorityQueue.isEmpty();
    }

    /**
     * Returns the number of items in the priority queue
     * @return the number of items in the priority queue
     */
    public int getSize() {
        return eventPriorityQueue.size();
    }
    
    /**
     * Returns a String with detail for each event in queue
     * @return a String with detail for each event in queue
     */
    public String toString() {
    	String eventString = "";
    	Object[] eventItems = eventPriorityQueue.toArray();
    	for (int i = 0; i < eventItems.length; i++) {
    		eventString += eventItems[i].toString() + "\n";
    	}
    	return eventString;
    }
}

/**
 * Used to sort events by their time of day
 */
class EventItemSorter implements Comparator<EventItem> {
    public int compare(EventItem a, EventItem b) { 
        return a.getTimeOfDay() - b.getTimeOfDay(); 
    } 
}