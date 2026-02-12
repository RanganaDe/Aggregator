package nl.kramp.assignment.service;

import java.time.LocalDate;
import nl.kramp.assignment.model.Availability;

public class AvailabilityService extends MockRemoteService<Availability>{
	public AvailabilityService() {
		super((pid, cid, market) ->
				Availability.of(43, "Gouda", LocalDate.of(2026, 2, 13)),
				100, 0.99);
	}

	// For tests
	public AvailabilityService(TriFunction<String, String, String, Availability> dataSupplier, int latency, double reliability) {
		super(dataSupplier, latency, reliability);
	}
}
