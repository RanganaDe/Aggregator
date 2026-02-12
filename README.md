# Product Information Aggregator

## Overview

This service aggregates product information from multiple upstream services (Catalog, Pricing, Availability, Customer) and returns a market- and customer-aware response. It demonstrates parallel execution, timeouts, and graceful degradation for unreliable services.

## How to Run the Service
	1.	Prerequisites: Java 17+ Gradle 8+
	2.	Build the project: ./gradlew build
	3.	Run the Spring Boot application: ./gradlew bootRun
	4.	Access the endpoint: GET http://localhost:8080/products/{productId}?market={marketCode}&customerId={customerId}

### Example:
```
GET http://localhost:8080/products/123?market=nl-NL&customerId=456

	•	productId → required
	•	marketCode → required (e.g., nl-NL, de-DE, pl-PL)
	•	customerId → optional
```

## Key Design Decisions
	1. Parallel execution using CompletableFuture
	•	All optional services (Pricing, Availability, Customer) are called in parallel to reduce total response time.
	•	Mandatory service (Catalog) is awaited; failure aborts the request.
	•	Timeouts are applied individually to avoid slow services blocking the response.
	2. Failure of one upstream service should not break the entire response
	•	Pricing fails → returns product with empty price detail
	•	Availability fails → returns product with empty availability detail
	•	Customer service fails or missing customer ID → returns product with empty customer details
	3. Mock services simulate latency and reliability
	•	Each mock service includes configurable latency and random failures to mimic real-world conditions.
	4.	Market-specific responses
	•	Catalog name and description are translated based on marketCode.
	•	Pricing includes currency logic per market (EUR for NL/DE, PLN for PL, USD fallback).

## Trade-offs:
	•	No persistence layer (DAOs/Repositories) to keep the project minimal.
	•	Models are used directly as API responses; DTOs are unnecessary for this assignment.
	•	No full localization or externalized translation files (hardcoded translations per market).

## What I Would Do Differently With More Time
	•	Use DTOs to decouple internal models from API responses.
	•	Add more tests to cover all edge cases.

## Answer to Design Question

### Option A: Add a Related Products service (200ms latency, 90% reliability)
	•	Optional vs Required: It should be optional, because the main product cannot be blocked if this service fails. Only catalog is mandatory.
	•	Integration:
	•	Add a new mock service RelatedProductsService.
	•	Call it in parallel using CompletableFuture in the aggregator.
	•	Apply a reasonable timeout (e.g., 250ms).
	•	On failure or timeout, return an empty list of related products.
	•	Advantages:
	•	Maintains aggregator responsiveness.
	•	Allows easy addition of new data sources without breaking existing clients.

#### Example Aggregation Flow (nl-NL)
	1.	Client requests: GET /products/124?market=nl-NL&customerId=456
	2.	Aggregator invokes:
	•	CatalogService → returns product info (mandatory)
	•	PricingService → returns price (optional, fallback: unavailable)
	•	AvailabilityService → returns stock (optional, fallback: unknown)
	•	CustomerService → returns personalized info (optional, fallback: standard)
	3.	Aggregator waits for all services in parallel, applies timeouts, combines results, and returns a single JSON response.

##### Example Response
```

{
  "availability": {
    "stockLevel": 43,
    "wareHouseLocation": "Gouda",
    "expectedDelivery": "2026-02-13"
  },
  "catalog": {
    "id": "124",
    "name": "Wiel",
    "description": "Maaierwiel",
    "specs": {},
    "images": []
  },
  "customerInfo": {
    "segment": "age",
    "customerId": "456",
    "preferences": {
      "newsLetterSubscribed": "true"
    }
  },
  "price": {
    "basePrice": 100,
    "customerDiscount": 10,
    "currency": "EUR",
    "finalPrice": 90
  }
}
```
