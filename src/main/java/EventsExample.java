import desmoj.core.simulator.*;
import desmoj.core.dist.*;

import java.util.concurrent.TimeUnit;

public class EventsExample extends Model {
    protected static int NUM_VC = 2;
    private ContDistExponential truckArrivalTime;
    private ContDistUniform serviceTime;
    protected Queue<Truck> truckQueue;
    protected Queue<VanCarrier> idleVCQueue;

    public EventsExample(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
    }

    public String description() {
        return "This model describes a queueing system located at a " +
                "container terminal. Trucks will arrive and " +
                "require the loading of a container. A van carrier (VC) is " +
                "on duty and will head off to find the required container " +
                "in the storage. It will then load the container onto the " +
                "truck. Afterwards, the truck leaves the terminal. " +
                "In case the VC is busy, the truck waits " +
                "for its turn on the parking-lot. " +
                "If the VC is idle, it waits on its own parking spot for the " +
                "truck to come.";
    }

    public void doInitialSchedules() {

        // create the TruckGeneratorEvent
        TruckGeneratorEvent truckGenerator =
                new TruckGeneratorEvent(this, "TruckGenerator", true);

        // schedule for start of simulation
        truckGenerator.schedule(new TimeSpan(0));
    }

    public void init() {
        serviceTime = new ContDistUniform(this, "ServiceTimeStream", 3.0, 7.0, true, false);
        truckArrivalTime = new ContDistExponential(this, "TruckArrivalTimeStream", 3.0, true, false);
        truckArrivalTime.setNonNegative(true);
        truckQueue = new Queue<Truck>(this, "Truck Queue", true, true);
        idleVCQueue = new Queue<VanCarrier>(this, "idle VC Queue", true, true);
        VanCarrier VC;
        for (int i = 0; i < NUM_VC; i++) {
            VC = new VanCarrier(this, "VanCarrier", true);
            idleVCQueue.insert(VC);
        }
    }

    public double getServiceTime() {
        return serviceTime.sample();
    }

    public double getTruckArrivalTime() {
        return truckArrivalTime.sample();
    }

    public static void main(java.lang.String[] args) {
        EventsExample model = new EventsExample(null, "EventsExample", true, true);
        Experiment exp = new Experiment("EventExampleExperiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(true);
        exp.stop(new TimeInstant(1500, TimeUnit.MINUTES));
        exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES));
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));
        exp.start();

        exp.report();
        exp.finish();
    }
}