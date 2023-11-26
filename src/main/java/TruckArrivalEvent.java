import desmoj.core.simulator.*;

import java.util.concurrent.TimeUnit;

public class TruckArrivalEvent extends Event<Truck> {
    private EventsExample myModel;

    public TruckArrivalEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (EventsExample) owner;
    }

    public void eventRoutine(Truck truck) {
        myModel.truckQueue.insert(truck);
        sendTraceNote("TruckQueueLength: " + myModel.truckQueue.length());

        if (!myModel.idleVCQueue.isEmpty()) {
            VanCarrier vanCarrier = myModel.idleVCQueue.first();
            myModel.idleVCQueue.remove(vanCarrier);
            myModel.truckQueue.remove(truck);

            ServiceEndEvent serviceEnd = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
            serviceEnd.schedule(vanCarrier, truck, new TimeSpan(myModel.getServiceTime(), TimeUnit.MINUTES));
        }
    }
}