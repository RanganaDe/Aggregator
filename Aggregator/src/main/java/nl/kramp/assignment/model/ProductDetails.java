package nl.kramp.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetails {
	private final Catalog catalog;
	private Price price;
	private Availability availability;
	private CustomerInfo customerInfo;

	private ProductDetails(Catalog catalog){
		this.catalog = catalog;
	}

	public static ProductDetails fromCatalog(Catalog catalog){
		return new ProductDetails(catalog);
	}

	public ProductDetails withPrice(Price price){
		this.price = price;
		return this;
	}

	public ProductDetails withAvailability(Availability availability){
		this.availability = availability;
		return this;
	}

	public ProductDetails withCustomerInfo(CustomerInfo customerInfo){
		this.customerInfo = customerInfo;
		return this;
	}

}
