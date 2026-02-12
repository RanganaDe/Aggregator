package nl.kramp.assignment.service;

import java.util.Collections;
import nl.kramp.assignment.model.Catalog;

public class CatalogService extends MockRemoteService<Catalog> {
	public CatalogService() {
		super((pid, cid, market) -> {
					String name = switch (market) {
						case "nl-NL" -> "Wiel";
						case "de-DE" -> "Rad";
						case "pl-PL" -> "Koło";
						default -> "Wheel";
					};

					String description = switch (market) {
						case "nl-NL" -> "Maaierwiel";
						case "de-DE" -> "Mäherrad";
						case "pl-PL" -> "Koło do kosiarki";
						default -> "Mower Wheel";
					};
					return new Catalog(pid, name, description, Collections.emptyMap(), Collections.emptyList());
				}, 50, 0.999);
	}

	// For tests
	public CatalogService(TriFunction<String, String, String, Catalog> dataSupplier, int latency, double reliability) {
		super(dataSupplier, latency, reliability);
	}

}
