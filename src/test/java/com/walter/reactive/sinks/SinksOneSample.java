package com.walter.reactive.sinks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

@SpringBootTest
public class SinksOneSample {
	private final static Logger log = LoggerFactory.getLogger(SinksOneSample.class);

	@Test
	@DisplayName("Sinks.One을 이용한 데이터 전달 # 1 - 단 하나의 데이터만 Subscriber에게 전달한다. 나머지는 Drop 된다.")
	public void sink_one_test_01() {
		final Sinks.One<String> sinkOne = Sinks.one();
		final Mono<String> mono = sinkOne.asMono();

		sinkOne.emitValue("Hello Reactor", FAIL_FAST);

		mono.subscribe(data -> log.info("Subscriber 1 : {}", data));
		mono.subscribe(data -> log.info("Subscriber 2 : {}", data));
	}

	@Test
	@DisplayName("Sinks.One을 이용한 데이터 전달 # 2 - 단 하나의 데이터만 Subscriber에게 전달한다. 나머지는 Drop 된다.")
	public void sink_one_test_02() {
		final Sinks.One<String> sinkOne = Sinks.one();
		final Mono<String> mono = sinkOne.asMono();

		sinkOne.emitValue("Hello Reactor", FAIL_FAST);
		sinkOne.emitValue("Hi Welcome to Hell Gate", FAIL_FAST);

		mono.subscribe(data -> log.info("Subscriber 1 : {}", data));
		mono.subscribe(data -> log.info("Subscriber 2 : {}", data));
	}
}
