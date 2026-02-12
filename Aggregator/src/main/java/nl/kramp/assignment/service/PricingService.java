package nl.kramp.assignment.service;

import nl.kramp.assignment.model.Price;

public class PricingService extends MockRemoteService<Price> {

	public PricingService() {
		super((pid, cid, market) -> {
			String currencyForMarket = switch(market) {
				case "nl-NL", "de-DE" -> "EUR";
				case "pl-PL" -> "PLN";
				default -> "USD";
			};
			if(cid != null && !cid.isEmpty()) {
				int marketBasedCustomerDiscount = switch(market) {
					case "nl-NL" -> 10;
					case "de-DE" -> 5;
					case "pl-PL" -> 15;
					default -> 0;
				};
				return Price.withCustomerDiscount(100, marketBasedCustomerDiscount, currencyForMarket);
			}
			return Price.of(100, currencyForMarket);
		}, 80, 0.995);
	}

	// For tests
	public PricingService(TriFunction<String, String, String, Price> dataSupplier, int latency, double reliability) {
		super(dataSupplier, latency, reliability);
	}

}
