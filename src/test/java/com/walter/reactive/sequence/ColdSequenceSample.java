package com.walter.reactive.sequence;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class ColdSequenceSample {
	private static final Logger log = LoggerFactory.getLogger(ColdSequenceSample.class);

	@Test
	public void coldSequence() {
		final Flux<String> coldFlux = Flux.fromIterable(List.of("RED", "YELLOW", "PINK"))
				.map(String::toLowerCase);

		coldFlux.subscribe(color -> log.info("# COLOR Subscriber 1 : {}", color));
		log.info("-----------------------");
		coldFlux.subscribe(color -> log.info("# COLOR Subscriber 2 : {}", color));
	}
}
