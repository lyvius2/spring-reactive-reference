package com.walter.reactive.scheduler;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

public class ParallelSample {
	private static final Logger log = LoggerFactory.getLogger(ParallelSample.class);

	@Test
	@DisplayName("Parallel Operator 테스트")
	public void parallelOperatorTest() {
		Flux.fromArray(List.of(1, 3, 5, 7, 9, 11, 13, 15).toArray())
				.parallel()
				.subscribe(num -> log.info("Thread Name : {}, {}, {}", ThreadUtils.getName(), LocalDateTime.now(), num));
	}

	@Test
	@DisplayName("Parallel Operator Multi Thread 테스트 (CPU 코어 갯수 별로 처리)")
	public void parallelOperatorMultiThreadTest() {
		Flux.fromArray(List.of(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21).toArray())
				.parallel()
				.runOn(Schedulers.parallel())
				.subscribe(num -> log.info("Thread Name : {}, {}, {}", ThreadUtils.getName(), LocalDateTime.now(), num));

		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("Parallel Operator Multi Thread 테스트 (스레드 갯수 지정)")
	public void parallelOperatorMultiThreadCustomTest() {
		Flux.fromArray(List.of(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21).toArray())
				.parallel(4)
				.runOn(Schedulers.parallel())
				.subscribe(num -> log.info("Thread Name : {}, {}, {}", ThreadUtils.getName(), LocalDateTime.now(), num));

		ThreadUtils.sleep(100L);
	}
}
