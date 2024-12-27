package com.walter.reactive.pubsub;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import org.slf4j.LoggerFactory;

public class PubSubSample {
	private final Logger log = LoggerFactory.getLogger(PubSubSample.class.getName());

	@Test
	@DisplayName("Publish Operator 테스트")
	void publishOperatorTest() {
		final ConnectableFlux<Integer> flux = Flux.range(1, 5)
				.delayElements(Duration.ofMillis(300L))
				.publish();

		ThreadUtils.sleep(500L);
		flux.subscribe(data -> log.info("# 1st subscriber : " + data));
		ThreadUtils.sleep(200L);
		flux.subscribe(data -> log.info("# 2nd subscriber : " + data));

		flux.connect();

		ThreadUtils.sleep(1_000L);
		flux.subscribe(data -> log.info("# 3rd subscriber : " + data));
		ThreadUtils.sleep(2_000L);
	}

	@Test
	@DisplayName("AutoConnect Operator 테스트")
	void autoConnectOperatorTest() {
		final Flux<String> flux = Flux.just("Concert part1", "Concert part2", "Concert part3")
				.delayElements(Duration.ofMillis(300L))
				.publish()
				.autoConnect(2);

		ThreadUtils.sleep(500L);
		flux.subscribe(data -> log.info("# 1st subscriber : {}", data));
		ThreadUtils.sleep(500L);
		flux.subscribe(data -> log.info("# 2nd subscriber : {}", data));

		ThreadUtils.sleep(500L);
		flux.subscribe(data -> log.info("# 3rd subscriber : {}", data));
		ThreadUtils.sleep(1_000L);
	}

	@Test
	@DisplayName("Replay Operator 테스트")
	void replayOperatorTest() {
		final ConnectableFlux<Integer> flux = Flux.range(1, 5)
				.delayElements(Duration.ofMillis(300L))
				.replay();

		ThreadUtils.sleep(500L);
		flux.subscribe(data -> log.info("# 1st subscriber : " + data));
		ThreadUtils.sleep(200L);
		flux.subscribe(data -> log.info("# 2nd subscriber : " + data));

		flux.connect();

		ThreadUtils.sleep(1_000L);
		flux.subscribe(data -> log.info("# 3rd subscriber : " + data));
		ThreadUtils.sleep(2_000L);
	}
}
