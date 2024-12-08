package com.walter.reactive.next;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import static com.walter.reactive.model.SampleData.BTC_TOP_PRICES_PER_YEAR;

public class NextOperatorSample {
	private static final Logger log = LoggerFactory.getLogger(NextOperatorSample.class);

	@Test
	@DisplayName("Next Operator 테스트")
	void nextOperatorTest() {
		Flux.fromIterable(BTC_TOP_PRICES_PER_YEAR)
				.doOnNext(data -> log.info("# emitted : {}", data))
				.filter(tuple -> tuple.getT1() == 2015)
				.next()
				.subscribe(data -> log.info("# result : {}, {}", data.getT1(), data.getT2()));
	}

	@Test
	@DisplayName("Non-Next Operator 테스트")
	void nonNextOperatorTest() {
		Flux.fromIterable(BTC_TOP_PRICES_PER_YEAR)
				.doOnNext(data -> log.info("# emitted : {}", data))
				.filter(tuple -> tuple.getT1() == 2015)
				.subscribe(data -> log.info("# result : {}, {}", data.getT1(), data.getT2()));
	}
}
