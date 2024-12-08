package com.walter.reactive.take;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class TakeOperatorSample {
	private static final Logger log = LoggerFactory.getLogger(TakeOperatorSample.class);

	@Test
	@DisplayName("Take Operator 테스트 #1")
	void takeOperatorTest_01() {
		final Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
				.doOnNext(data -> log.info("# emitted : {}", data))
				.take(3);

		flux.subscribe(data -> log.info("# result : {}", data));
		ThreadUtils.sleep(5_000L);
	}

	@Test
	@DisplayName("Take Operator 테스트 #2")
	void takeOperatorTest_02() {
		final Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
				.doOnNext(data -> log.info("# emitted : {}", data))
				.take(Duration.ofMillis(2_500));

		flux.subscribe(data -> log.info("# result : {}", data));
		ThreadUtils.sleep(4_000L);
	}
}
