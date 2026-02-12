package nl.kramp.assignment.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Price {
	private  Integer basePrice;
	private Integer customerDiscount;
	private Integer finalPrice;
	private String currency;

	public Price(Integer basePrice, Integer customerDiscount, String currency){
		this.basePrice = basePrice;
		this.customerDiscount = customerDiscount;
		this.finalPrice = basePrice != null ? basePrice - (customerDiscount != null ? customerDiscount : 0) : null;
		this.currency = currency;
	}

	public static Price of(Integer price, String currency){
		return new Price(price, null, currency);
	}

	public static Price withCustomerDiscount(Integer basePrice,Integer customerDiscount, String currency){
		return new Price(basePrice, customerDiscount, currency);
	}

	public static Price unavailable(){
		return new Price(null, null, null);
	}
}
