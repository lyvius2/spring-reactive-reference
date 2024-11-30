package com.walter.reactive.sinks;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class SinksManySample {
	private final static Logger log = LoggerFactory.getLogger(SinksManySample.class);

	@Test
	@DisplayName("Sinks.Many을 이용한 데이터 전달 # 1 - unicast()를 사용해서 단 하나의 Subscriber에게만 전달한다.")
	public void sink_many_test_01() {
		final Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
		final Flux<Integer> fluxView = unicastSink.asFlux();

		unicastSink.emitNext(1, FAIL_FAST);
		unicastSink.emitNext(2, FAIL_FAST);

		fluxView.subscribe(data -> log.info("Subscriber 1 : {}", data));
		unicastSink.emitNext(3, FAIL_FAST);
		fluxView.subscribe(data -> log.info("Subscriber 2 : {}", data));
	}

	@Test
	@DisplayName("Sink.Many을 이용한 데이터 전달 # 2 - multicast()를 사용해서 여러 Subscriber에게 전달한다.")
	public void sink_many_test_02() {
		final Sinks.Many<Integer> multicastSink = Sinks.many().multicast().onBackpressureBuffer();
		final Flux<Integer> fluxView = multicastSink.asFlux();

		multicastSink.emitNext(1, FAIL_FAST);
		multicastSink.emitNext(2, FAIL_FAST);

		fluxView.subscribe(data -> log.info("Subscriber 1 : {}", data));
		fluxView.subscribe(data -> log.info("Subscriber 2 : {}", data));
		multicastSink.emitNext(3, FAIL_FAST);
	}

	@Test
	@DisplayName("Sink.Many을 이용한 데이터 전달 # 3 - replay()를 사용해서 여러 Subscriber에게 전달한다.")
	public void sink_many_test_03() {
		// 구독 이후, emit 된 데이터 중에서 최신 데이터 2개만 replay 된다.
		final Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2);
		final Flux<Integer> fluxView = replaySink.asFlux();

		replaySink.emitNext(1, FAIL_FAST);
		replaySink.emitNext(2, FAIL_FAST);
		replaySink.emitNext(3, FAIL_FAST);

		fluxView.subscribe(data -> log.info("Subscriber 1 : {}", data));
		fluxView.subscribe(data -> log.info("Subscriber 2 : {}", data));
	}

	@Test
	@DisplayName("Sink.Many을 이용한 데이터 전달 # 4 - replay()를 사용해서 최신 emitted된 데이터만 여러 Subscriber에게 전달한다.")
	public void sink_many_test_04() {
		final Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2);
		final Flux<Integer> fluxView = replaySink.asFlux();

		replaySink.emitNext(1, FAIL_FAST);
		replaySink.emitNext(2, FAIL_FAST);
		replaySink.emitNext(3, FAIL_FAST);

		fluxView.subscribe(data -> log.info("Subscriber 1 : {}", data));
		replaySink.emitNext(4, FAIL_FAST);
		fluxView.subscribe(data -> log.info("Subscriber 2 : {}", data));
	}

	@Test
	@DisplayName("Sink.Many을 이용한 데이터 전달 # 5 - replay()를 사용해서 emitted된 모든 데이터를 여러 Subscriber에게 전달한다.")
	public void sink_many_test_05() {
		final Sinks.Many<Integer> replaySink = Sinks.many().replay().all();
		final Flux<Integer> fluxView = replaySink.asFlux();

		replaySink.emitNext(1, FAIL_FAST);
		replaySink.emitNext(2, FAIL_FAST);
		replaySink.emitNext(3, FAIL_FAST);

		fluxView.subscribe(data -> log.info("Subscriber 1 : {}", data));
		fluxView.subscribe(data -> log.info("Subscriber 2 : {}", data));
		ThreadUtils.sleep(1_000L);
	}
}
