package nl.kramp.assignment.service;

import java.util.Map;
import nl.kramp.assignment.model.CustomerInfo;

public class CustomerService extends MockRemoteService<CustomerInfo> {
	public CustomerService() {
		super(((pid, cid, market) -> {
					if(cid == null || cid.isEmpty()) {
						return CustomerInfo.standard();
					} else {
						return CustomerInfo.of("age", cid, Map.of("newsLetterSubscribed", "true"));
					}
				}), 60, 0.99);
	}

	public CustomerService(TriFunction<String, String, String, CustomerInfo> dataSupplier, int latency, double reliability) {
		super(dataSupplier, latency, reliability);
	}
}
