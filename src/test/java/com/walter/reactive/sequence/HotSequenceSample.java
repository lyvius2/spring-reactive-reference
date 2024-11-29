package com.walter.reactive.sequence;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class HotSequenceSample {
	private static final Logger log = LoggerFactory.getLogger(HotSequenceSample.class);

	@Test
	public void hotSequence() {
		final Flux<String> concertFlux = Flux.fromStream(Stream.of("Singer A", "Singer B", "Singer C", "Singer D", "Singer E"))
				.delayElements(Duration.ofSeconds(1))
				.share();

		concertFlux.subscribe(singer -> log.info("# Subscriber1 is watching : {}", singer));

		ThreadUtils.sleep(2_500);

		concertFlux.subscribe(singer -> log.info("# Subscriber2 is watching : {}", singer));

		ThreadUtils.sleep(3_000);
	}
}
