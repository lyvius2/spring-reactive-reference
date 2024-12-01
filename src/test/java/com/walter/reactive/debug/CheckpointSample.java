package com.walter.reactive.debug;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class CheckpointSample {
	private static final Logger log = LoggerFactory.getLogger(CheckpointSample.class);

	@Test
	@DisplayName("Checkpoint Operator 테스트 #1")
	public void checkpointOperatorTest_01() {
		Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
				.checkpoint()
				.map(num -> num + 2)
				.subscribe(
						result -> log.info("# result : {}", result),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Checkpoint Operator 테스트 #2")
	public void checkpointOperatorTest_02() {
		Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
				.map(num -> num + 2)
				.checkpoint()
				.subscribe(
						result -> log.info("# result : {}", result),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Checkpoint Operator 테스트 #3")
	public void checkpointOperatorTest_03() {
		Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
				.checkpoint()
				.map(num -> num + 2)
				.checkpoint()
				.subscribe(
						result -> log.info("# result : {}", result),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Checkpoint Operator 테스트 #4")
	public void checkpointOperatorTest_04() {
		Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
				.checkpoint("CheckpointSample.zipWith.checkpoint")
				.map(num -> num + 2)
				.subscribe(
						result -> log.info("# result : {}", result),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Checkpoint Operator 테스트 #5")
	public void checkpointOperatorTest_05() {
		Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
				.checkpoint("CheckpointSample.zipWith.checkpoint")
				.map(num -> num + 2)
				.checkpoint("CheckpointSample.map.checkpoint")
				.subscribe(
						result -> log.info("# result : {}", result),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}
}
