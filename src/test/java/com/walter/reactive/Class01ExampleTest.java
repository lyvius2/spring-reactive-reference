package com.walter.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Class01ExampleTest {
	@Test
	public void helloReactorTest() {
		Flux<String> sequence = Flux.just("Hello", "Reactor");
		sequence.map(data -> data.toUpperCase())
				.subscribe(data -> System.out.println(data));
	}
}
