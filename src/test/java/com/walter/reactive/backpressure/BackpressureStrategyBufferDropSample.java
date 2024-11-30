package com.walter.reactive.backpressure;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyBufferDropSample {
	private final static Logger log = LoggerFactory.getLogger(BackpressureStrategyBufferDropSample.class);

	@Test
	@DisplayName("Downstream으로 전달하는 데이터가 Buffer에 가득 차면 가장 최근에 Buffer 로 들어온 데이터부터 Drop하는 예제")
	public void buffer_drop_latest_strategy_test() {
		Flux.interval(Duration.ofMillis(300L))
				.doOnNext(data -> log.info("# emitted by original Flux : {}", data))
				.onBackpressureBuffer(2,
						dropped -> log.info("# Overflow & Dropped : {}", dropped),
						BufferOverflowStrategy.DROP_LATEST)
				.doOnNext(data -> log.info("# emitted by Buffer : {}", data))
				.publishOn(Schedulers.parallel(), false, 1)
				.subscribe(data -> {
						ThreadUtils.sleep(1_000L);
						log.info("# data : {}", data);
					},
					error -> log.info("# 에러 발생 : {}", error.toString()));
		ThreadUtils.sleep(3_000L);
	}

	@Test
	@DisplayName("Downstream으로 전달하는 데이터가 Buffer에 가득 차면 Buffer 로 들어온 가장 오래된 데이터부터 Drop하는 예제")
	public void buffer_drop_oldest_strategy_test() {
		Flux.interval(Duration.ofMillis(300L))
				.doOnNext(data -> log.info("# emitted by original Flux : {}", data))
				.onBackpressureBuffer(2,
						dropped -> log.info("# Overflow & Dropped : {}", dropped),
						BufferOverflowStrategy.DROP_OLDEST)
				.doOnNext(data -> log.info("# emitted by Buffer : {}", data))
				.publishOn(Schedulers.parallel(), false, 1)
				.subscribe(data -> {
							ThreadUtils.sleep(1_000L);
							log.info("# data : {}", data);
						},
						error -> log.info("# 에러 발생 : {}", error.toString()));
		ThreadUtils.sleep(3_000L);
	}
}
