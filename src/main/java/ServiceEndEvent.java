import desmoj.core.simulator.*;

import java.util.concurrent.TimeUnit;

public class ServiceEndEvent extends EventOf2Entities<VanCarrier, Truck> {
    private EventsExample myModel;

    public ServiceEndEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (EventsExample) owner;
    }

    public void eventRoutine(VanCarrier vanCarrier, Truck truck) {
        sendTraceNote(truck + " leaves the terminal");
        if (!myModel.truckQueue.isEmpty()) {
            Truck nextTruck = myModel.truckQueue.first();
            myModel.truckQueue.remove(nextTruck);

            ServiceEndEvent event = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
            event.schedule(vanCarrier, nextTruck, new TimeSpan(myModel.getServiceTime(), TimeUnit.MINUTES));
        } else {
            myModel.idleVCQueue.insert(vanCarrier);
        }
    }
}