package nl.kramp.assignment.model;

import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInfo {
	private String segment;
	private String customerId;
	private Map<String, String> preferences;

	public static CustomerInfo of(String segment, String customerId, Map<String, String> preferences) {
		if(customerId != null && !customerId.isEmpty()) {
			return new CustomerInfo(segment, customerId, preferences);
		} else {
			return new CustomerInfo(segment, customerId, null);
		}
	}

	public static CustomerInfo standard() {
		return new CustomerInfo(null, null, Collections.emptyMap());
	}
}
