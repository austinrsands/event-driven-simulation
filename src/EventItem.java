/**
 * Represents and event
 */
public class EventItem {
    private int timeOfDay;
    private int serviceTime;
    private int typeOfEvent;

    /**
     * Creates an event item instance
     * @param timeOfDay the time of day
     * @param serviceTime the duration of service
     * @param typeOfEvent the type of event, -1 being arrival, 0..9 being departure
     */
    public EventItem(int timeOfDay, int serviceTime, int typeOfEvent) {
        this.timeOfDay = timeOfDay;
        this.serviceTime = serviceTime;
        this.typeOfEvent = typeOfEvent;
    }

    /**
     * Gets the time of day
     * @return the time of day
     */
    public int getTimeOfDay() {
        return timeOfDay;
    }

    /**
     * Gets the duration of service
     * @return the duration of service
     */
    public int getServiceTime() {
        return serviceTime;
    }

    /**
     * Gets the type of event, -1 being arrival, 0..9 being departure
     * @return the type of event, -1 being arrival, 0..9 being departure
     */
    public int getTypeOfEvent() {
        return typeOfEvent;
    }
    
    /**
     * Returns a sring containing the details of the event
     * @return string containing details of event
     */
    public String toString() {
    	return "Time Of Day: " + timeOfDay + ", Service Time: " + serviceTime + ", Type of Event: " + typeOfEvent;
    }
}