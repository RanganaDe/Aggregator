package nl.kramp.assignment;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import nl.kramp.assignment.model.Availability;
import nl.kramp.assignment.model.Catalog;
import nl.kramp.assignment.model.CustomerInfo;
import nl.kramp.assignment.model.Price;
import nl.kramp.assignment.model.ProductDetails;
import nl.kramp.assignment.service.AvailabilityService;
import nl.kramp.assignment.service.CatalogService;
import nl.kramp.assignment.service.CustomerService;
import nl.kramp.assignment.service.PricingService;
import nl.kramp.assignment.service.ProductInformationAggregatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductInformationAggregatorServiceTest {
	private final String productId = "132";
	private final String marketCode = "nl-NL";
	private final String customerId = "1010";

	@Test
	void shouldReturnFullProductDetailsWhenAllServicesSucceed() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService();
		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, marketCode, customerId);

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());

		Availability expectedAvailability = new Availability(43, "Gouda", LocalDate.of(2026, 2, 13));
		Assertions.assertEquals(expectedAvailability, actual.getAvailability());

		CustomerInfo expectedCustomerInfo = new CustomerInfo("age", "1010", Map.of("newsLetterSubscribed", "true"));
		Assertions.assertEquals(expectedCustomerInfo, actual.getCustomerInfo());

		Price expectedPrice = new Price(100, 10, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

	}

	@Test
	void shouldReturnCorrectTranslationAndRegionPriceLogicForPolishMarketCode() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService();
		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, "pl-PL", customerId);

		Price expectedPrice = new Price(100, 15, "PLN");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		Catalog expectedCatalog = new Catalog("132", "Koło", "Koło do kosiarki", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());
	}

	@Test
	void shouldReturnCorrectTranslationAndRegionPriceLogicForDutchMarketCode() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService();
		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, "nl-NL", customerId);

		Price expectedPrice = new Price(100, 10, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());
	}

	@Test
	void shouldReturnCorrectTranslationAndRegionPriceLogicForGermanMarketCode() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService();
		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, "de-DE", customerId);

		Price expectedPrice = new Price(100, 5, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		Catalog expectedCatalog = new Catalog("132", "Rad", "Mäherrad", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());
	}

	@Test
	void shouldReturnEmptyCustomerDetailOnProductWhenCustomerIdIsNotProvided() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService();
		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, marketCode, null);

		CustomerInfo expectedCustomerInfo = new CustomerInfo(null, null, Collections.emptyMap());
		Assertions.assertEquals(expectedCustomerInfo, actual.getCustomerInfo());

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());

		Availability expectedAvailability = new Availability(43, "Gouda", LocalDate.of(2026, 2, 13));
		Assertions.assertEquals(expectedAvailability, actual.getAvailability());

		Price expectedPrice = new Price(100, null, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());
	}

	@Test
	void shouldReturnEmptyPriceDetailOnProductWhenPricingServiceFails() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService(
				new CustomerService(),
				new AvailabilityService(),
				new CatalogService(),
				new PricingService(
						(pid, cid, market) -> {throw new RuntimeException();}
						, 80, 0.995));

		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, marketCode, customerId);

		Price expectedPrice = new Price(null, null, null);
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		CustomerInfo expectedCustomerInfo = new CustomerInfo("age", "1010", Map.of("newsLetterSubscribed", "true"));
		Assertions.assertEquals(expectedCustomerInfo, actual.getCustomerInfo());

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());

		Availability expectedAvailability = new Availability(43, "Gouda", LocalDate.of(2026, 2, 13));
		Assertions.assertEquals(expectedAvailability, actual.getAvailability());

	}

	@Test
	void shouldReturnEmptyAvailabilityDetailOnProductWhenAvailabilityServiceFails() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService(
				new CustomerService(),
				new AvailabilityService((pid, cid, market) -> {throw new RuntimeException();}
						, 100, 0.99),
				new CatalogService(),
				new PricingService());

		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, marketCode, customerId);

		Availability expectedAvailability = new Availability(null, null, null);
		Assertions.assertEquals(expectedAvailability, actual.getAvailability());

		Price expectedPrice = new Price(100, 10, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		CustomerInfo expectedCustomerInfo = new CustomerInfo("age", "1010", Map.of("newsLetterSubscribed", "true"));
		Assertions.assertEquals(expectedCustomerInfo, actual.getCustomerInfo());

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());

	}

	@Test
	void shouldReturnEmptyCustomerDetailOnProductWhenAvailabilityServiceFails() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService(
				new CustomerService((pid, cid, market) -> {throw new RuntimeException();}, 60, 0.99),
				new AvailabilityService(),
				new CatalogService(),
				new PricingService());

		ProductDetails actual = productInformationAggregatorService.getProductDetails(productId, marketCode, customerId);

		CustomerInfo expectedCustomerInfo = new CustomerInfo(null, null, Collections.emptyMap());
		Assertions.assertEquals(expectedCustomerInfo, actual.getCustomerInfo());

		Availability expectedAvailability = new Availability(43, "Gouda", LocalDate.of(2026, 2, 13));
		Assertions.assertEquals(expectedAvailability, actual.getAvailability());

		Price expectedPrice = new Price(100, 10, "EUR");
		Assertions.assertEquals(expectedPrice, actual.getPrice());

		Catalog expectedCatalog = new Catalog("132", "Wiel", "Maaierwiel", Collections.emptyMap(), Collections.emptyList());
		Assertions.assertEquals(expectedCatalog, actual.getCatalog());
	}

	@Test
	void shouldFailOnProductWhenCatalogServiceFails() {
		ProductInformationAggregatorService productInformationAggregatorService = new ProductInformationAggregatorService(
				new CustomerService(),
				new AvailabilityService(),
				new CatalogService((pid, cid, market) -> {throw new RuntimeException();}
						, 80, 0.995),
				new PricingService());

		Assertions.assertThrows(RuntimeException.class,
				() -> productInformationAggregatorService.getProductDetails(productId, marketCode, customerId));

	}

}
