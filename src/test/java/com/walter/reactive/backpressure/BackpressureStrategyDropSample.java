package com.walter.reactive.backpressure;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyDropSample {
	private final static Logger log = LoggerFactory.getLogger(BackpressureStrategyDropSample.class);

	@Test
	@DisplayName("Downstream으로 전달하는 데이터가 Buffer에 가득 차면 Buffer 밖에서 대기하는 데이터를 Drop하는 예제")
	public void drop_strategy_test() {
		Flux.interval(Duration.ofMillis(1L))
				.onBackpressureDrop(dropped -> log.info("# Dropped : {}", dropped))
				.publishOn(Schedulers.parallel())
				.subscribe(data -> {
						ThreadUtils.sleep(5L);
						log.info("# data : {}", data);
					},
					error -> log.error("# 에러 발생 : {}", error.toString())
				);
		ThreadUtils.sleep(2_000);
	}
}
