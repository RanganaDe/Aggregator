package nl.kramp.assignment.controller;

import java.util.Set;
import nl.kramp.assignment.model.ProductDetails;
import nl.kramp.assignment.service.ProductInformationAggregatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductInformationAggregatorService productInformationAggregatorService;

	public ProductController(ProductInformationAggregatorService productInformationAggregatorService) {
		this.productInformationAggregatorService = productInformationAggregatorService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDetails> getProductDetails(@PathVariable("id") String productId,
															@RequestParam("market") String marketCode,
															@RequestParam(value = "customerId", required = false) String customerId) {
		validateInputs(productId, marketCode);
		return ResponseEntity.ok(productInformationAggregatorService.getProductDetails(productId, marketCode, customerId));
	}

	private void validateInputs(String productId, String marketCode) {
		Set<String> supportedMarkets = Set.of("nl-NL", "de-DE", "pl-PL");
		if (!supportedMarkets.contains(marketCode)) {
			throw new IllegalArgumentException("Unsupported market: " + marketCode);
		}
		if (productId == null || productId.isBlank()) {
			throw new IllegalArgumentException("Product Id is required");
		}
	}
}
