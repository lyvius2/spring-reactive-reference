package com.walter.reactive.skip;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class SkipOperatorSample {
	private final static Logger log = LoggerFactory.getLogger(SkipOperatorSample.class);

	@Test
	@DisplayName("Skip Operator 테스트 #1")
	void skipOperatorTest_01() {
		final Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
				.doOnNext(data -> log.info("# emitted : {}", data))
				.skip(3);

		flux.subscribe(data -> log.info("# result : {}", data));
		ThreadUtils.sleep(5_000L);
	}

	@Test
	@DisplayName("Skip Operator 테스트 #2")
	void skipOperatorTest_02() {
		final Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
				.doOnNext(data -> log.info("# emitted : {}", data))
				.skip(Duration.ofMillis(2_500));

		flux.subscribe(data -> log.info("# result : {}", data));
		ThreadUtils.sleep(5_000L);
	}

	@Test
	@DisplayName("Skip Operator 테스트 #3")
	void skipOperatorTest_03() {
		final Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
				.skipUntilOther(doSomeTask());

		flux.subscribe(data -> log.info("# result : {}", data));
		ThreadUtils.sleep(4_000L);
	}

	private Publisher<?> doSomeTask() {
		return Mono.empty().delay(Duration.ofMillis(2_500));
	}
}
