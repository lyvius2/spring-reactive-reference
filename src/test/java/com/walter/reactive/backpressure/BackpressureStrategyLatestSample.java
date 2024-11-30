package com.walter.reactive.backpressure;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyLatestSample {
	private final static Logger log = LoggerFactory.getLogger(BackpressureStrategyLatestSample.class);

	@Test
	@DisplayName("Downstream으로 전달하는 데이터가 Buffer에 가득 차면 가장 최근에 emit된 데이터부터 Buffer에 채우는 전략")
	public void latest_strategy_test() {
		Flux.interval(Duration.ofMillis(1L))
				.onBackpressureLatest()
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
