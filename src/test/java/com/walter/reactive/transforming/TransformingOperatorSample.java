package com.walter.reactive.transforming;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

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

	@Test
	@DisplayName("Concat Operator 테스트")
	void concatOperatorTest() {
		Flux.concat(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
				Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
		).subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(3_500L);
	}

	@Test
	@DisplayName("Merge Operator 테스트")
	void mergeOperatorTest() {
		Flux.merge(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
				Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
		).subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(3_500L);
	}

	@Test
	@DisplayName("Zip Operator 테스트 #1")
	void zipOperatorTest01() {
		Flux.zip(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
				Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
		).subscribe(tuple2 -> log.info("# onNext : {}", tuple2));

		ThreadUtils.sleep(2_500L);
	}

	@Test
	@DisplayName("Zip Operator 테스트 #2")
	void zipOperatorTest02() {
		Flux.zip(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
				Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)),
				(n1, n2) -> n1 * n2
		).subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(2_500L);
	}
}
