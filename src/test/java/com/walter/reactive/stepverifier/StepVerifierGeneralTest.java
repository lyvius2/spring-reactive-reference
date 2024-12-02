package com.walter.reactive.stepverifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StepVerifierGeneralTest {
	@Test
	public void sayHelloReactorTest() {
		final String helloReactor = "Hello, Reactor!";

		StepVerifier
				.create(Mono.just(helloReactor))
				.expectNext("Hello, Reactor!")
				.expectComplete()
				.verify();
	}

	@Test
	public void doubleSayHelloReactorTest() {
		final Flux<String> flux = Flux.just("Hello", "Reactor!");

		StepVerifier
				.create(flux)
				.expectSubscription()
				.expectNext("Hello")
				.expectNext("Reactor!")
				.expectComplete()
				.verify();
	}

	@Test
	public void doubleSayHelloReactorDescriptionTest() {
		final Flux<String> flux = Flux.just("Hello", "Reactor!");

		StepVerifier
				.create(flux)
				.expectSubscription()
				.as("# expect subscription")
				.expectNext("Hi")
				.as("# expect Hi")
				.expectNext("Reactor!")
				.as("# expect Reactor!")
				.expectComplete()
				.verify();
	}

	@Test
	public void executeErrorTest() {
		final Flux<Integer> source = Flux.just(2, 4, 6, 8, 10);

		StepVerifier
				.create(source.zipWith(Flux.just(1, 2, 3, 4, 0), (a, b) -> a / b))
				.expectSubscription()
				.expectNext(2)
				.expectNext(2)
				.expectNext(2)
				.expectNext(2)
				.expectError()
				.verify();
	}
}
