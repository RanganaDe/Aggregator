package nl.kramp.assignment.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class MockRemoteService<T> {
	private final int latencyMs;
	private final double reliability;
	private final Random random = new Random();
	private final TriFunction<String, String, String, T> dataSupplier;

	protected MockRemoteService(TriFunction<String, String, String, T> dataSupplier, int latencyMs, double reliability) {
		this.latencyMs = latencyMs;
		this.reliability = reliability;
		this.dataSupplier = dataSupplier;
	}

	public T call(String productId, String customerId, String marketCode) {
		simulateLatency();
		simulateFailure();
		return dataSupplier.apply(productId, customerId, marketCode);
	}

	private void simulateFailure() {
		double nextDouble = random.nextDouble();
		System.out.println("Random next double is: " + nextDouble);
		if(nextDouble > reliability) {
			throw new RuntimeException("Downstream service failure");
		}
	}

	private void simulateLatency() {
		try {
			TimeUnit.MILLISECONDS.sleep(latencyMs);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted");
		}
	}

	@FunctionalInterface
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}
}
