package nl.kramp.assignment.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Availability {
	private Integer stockLevel;
	private String wareHouseLocation;
	private LocalDate expectedDelivery;

	public static Availability of(int stockLevel, String wareHouseLocation, LocalDate expectedDeliveryTimestamp){
		return new Availability(stockLevel, wareHouseLocation, expectedDeliveryTimestamp );
	}

	public static Availability unknown(){
		return new Availability(null, null, null);
	}
}
