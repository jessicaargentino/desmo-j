import desmoj.core.simulator.*;

import java.util.concurrent.TimeUnit;

public class TruckGeneratorEvent extends ExternalEvent {
    public TruckGeneratorEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    public void eventRoutine() {
        EventsExample model = (EventsExample) getModel();
        Truck truck = new Truck(model, "Truck", true);
        TruckArrivalEvent truckArrival = new TruckArrivalEvent(model, "TruckArrivalEvent", true);
        truckArrival.schedule(truck, new TimeSpan(0.0));

        schedule(new TimeSpan(model.getTruckArrivalTime(), TimeUnit.MINUTES));
    }
}