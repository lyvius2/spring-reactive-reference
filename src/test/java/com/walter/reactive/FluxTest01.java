package com.walter.reactive;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxTest01 {
	private static final Logger log = LoggerFactory.getLogger(FluxTest01.class);

	@Test
	public void helloReactorTest() {
		Flux<String> sequence = Flux.just("Hello", "Reactor");
		sequence.map(data -> data.toUpperCase())
				.subscribe(data -> System.out.println(data));
	}

	@Test
	public void multiFluxTest() {
		Flux.just(6, 9, 13)
				.map(num -> num % 2)
				.subscribe(remainder -> log.info("# remainder : {}", remainder));
	}

	@Test
	public void arrayFluxTest() {
		Flux.fromArray(new Integer[]{3, 6, 7, 9})
				.filter(num -> num > 6)
				.map(num -> num * 2)
				.subscribe(multiply -> log.info("# multiply : {}", multiply));
	}

	@Test
	public void monoFluxTest() {
		Flux<Object> flux =
				Mono.justOrEmpty(null)
						.concatWith(Mono.justOrEmpty("Hello Flux"));
		flux.subscribe(data -> log.info("# result : {}", data));
	}

	@Test
	public void concatFluxTest() {
		Flux.concat(
				Flux.just("Venus"),
				Flux.just("Earth"),
				Flux.just("Mars")
		)
				.collectList()
				.subscribe(list -> log.info("# solar system : {}", list));
	}
}
