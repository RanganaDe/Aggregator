package nl.kramp.assignment.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import nl.kramp.assignment.model.Availability;
import nl.kramp.assignment.model.CustomerInfo;
import nl.kramp.assignment.model.Price;
import nl.kramp.assignment.model.ProductDetails;
import org.springframework.stereotype.Service;

@Service
public class ProductInformationAggregatorService {
	private final CatalogService catalogService;
	private final PricingService pricingService;;
	private final CustomerService customerService;
	private final AvailabilityService availabilityService;
	private final Executor executor = Executors.newFixedThreadPool(4);

	public ProductInformationAggregatorService() {
		this.customerService = new CustomerService();
		this.availabilityService = new AvailabilityService();
		this.catalogService = new CatalogService();
		this.pricingService = new PricingService();
	}

	public ProductInformationAggregatorService(CustomerService customerService, AvailabilityService availabilityService, CatalogService catalogService, PricingService pricingService ) {
		this.customerService = customerService;
		this.availabilityService = availabilityService;
		this.catalogService = catalogService;
		this.pricingService = pricingService;
	}
	public ProductDetails getProductDetails(String productId, String marketCode, String customerId) {

		CompletableFuture<ProductDetails> base =
				CompletableFuture.supplyAsync(() -> catalogService.call(productId, customerId, marketCode), executor)
						.orTimeout(110, TimeUnit.MILLISECONDS).thenApply(ProductDetails::fromCatalog);

		CompletableFuture<Price> price =
				CompletableFuture.supplyAsync(() -> pricingService.call(productId, customerId, marketCode), executor)
						.orTimeout(110, TimeUnit.MILLISECONDS)
						.exceptionally(ex -> Price.unavailable());

		CompletableFuture<Availability> availability =
				CompletableFuture.supplyAsync(() -> availabilityService.call(productId, customerId, marketCode), executor)
						.orTimeout(110, TimeUnit.MILLISECONDS)
						.exceptionally(ex -> Availability.unknown());

		CompletableFuture<CustomerInfo> customerInfo =
				CompletableFuture.supplyAsync(() -> customerService.call(productId, customerId, marketCode), executor)
						.orTimeout(110, TimeUnit.MILLISECONDS)
						.exceptionally(ex -> CustomerInfo.standard());

		return base.thenCombine(price,ProductDetails::withPrice)
				.thenCombine(availability, ProductDetails::withAvailability)
				.thenCombine(customerInfo, ProductDetails::withCustomerInfo)
				.join();
	}

}
