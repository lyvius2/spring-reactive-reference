package com.walter.reactive.context;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ContextSample {
	private final static Logger log = LoggerFactory.getLogger(ContextSample.class);

	@Test
	@DisplayName("Context 저장 / 조회 테스트")
	public void contextTest() {
		final String key = "message";
		final Mono<String> mono = Mono.deferContextual(context ->
					Mono.just("Hello" + " " + context.get(key)).doOnNext(log::info)
				)
				.subscribeOn(Schedulers.boundedElastic())
				.publishOn(Schedulers.parallel())
				.transformDeferredContextual((mono2, context) -> mono2.map(data -> data + " " + context.get(key)))
				.contextWrite(context -> context.put(key, "Reactor"));

		mono.subscribe(data -> log.info("# data : {}", data));
		ThreadUtils.sleep(100L);
	}
}
