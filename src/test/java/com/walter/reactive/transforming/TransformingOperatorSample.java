package com.walter.reactive.transforming;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class TransformingOperatorSample {
	private static final Logger log = LoggerFactory.getLogger(TransformingOperatorSample.class);

	@Test
	@DisplayName("FlatMap Operator 테스트")
	void flatMapOperatorTest() {
		Flux.just("Good", "Bad")
			 .flatMap(feeling -> Flux.just("Morning", "Afternoon", "Evening")
					                 .map(time -> feeling + " " + time)
			 )
			 .subscribe(data -> log.info("# result : {}", data));
	}

	@Test
	@DisplayName("Parallel 동작")
	void flatMapParallelTest() {
		Flux.range(2, 8)
			.flatMap(dan -> Flux.range(1, 9)
								.publishOn(Schedulers.parallel())
								.map(n -> dan + " * " + n + " = " + dan * n)
			)
			.subscribe(log::info);
	}
}
