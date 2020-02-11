public class Main {
    
    /**
     * Entry point of the program
     * @param args command line arguments
     */
    public static void main(String args[]) {
        Main instance = new Main();
        instance.run();
    }

    /**
     * Driver for simulation
     */
    public void run() {
    	// Simulation(numTellers, arrivalTimeMean, arrivalTimeVariance, serviceTimeMean, serviceTimeVariance, timeLimit)
        Simulation sim = new Simulation(5, 3, 2, 8, 3, 300);
        sim.run();
    }
}