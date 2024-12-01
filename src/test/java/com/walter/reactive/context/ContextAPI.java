package com.walter.reactive.context;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextAPI {
	private final static Logger log = LoggerFactory.getLogger(ContextAPI.class);

	@Test
	@DisplayName("Context.of(...) 를 사용한 Context write 테스트")
	public void contextOfTest() {
		final String key1 = "id";
		final String key2 = "console";
		final Mono<String> mono = Mono.deferContextual(context -> Mono.just("ID : " + context.get(key1) + ", CONSOLE : " + context.get(key2)))
				.publishOn(Schedulers.parallel())
				.contextWrite(Context.of(key1, 1, key2, "PlayStation"));

		mono.subscribe(data -> log.info("# data : {}", data));
		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("Context.putAll(...) 를 사용한 Context write 테스트")
	public void contextPutAllTest() {
		final String key1 = "id";
		final String key2 = "console";
		final String key3 = "company";

		final Mono<String> mono = Mono.deferContextual(context ->
						Mono.just("ID : " + context.get(key1) + ", CONSOLE : " + context.get(key2) + ", COMPANY : " + context.get(key3))
				)
				.publishOn(Schedulers.parallel())
				.contextWrite(context -> context.putAll(Context.of(key2, "DreamCast", key3, "SEGA").readOnly()))
				.contextWrite(context -> context.put(key1, 2));

		mono.subscribe(data -> log.info("# data : {}", data));
		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("Context.getOrDefault(...) 를 사용한 Context read 테스트")
	public void contextGetOrDefaultTest() {
		final String key1 = "id";
		final String key2 = "console";

		final Mono<String> mono = Mono.deferContextual(context ->
						Mono.just("ID : " + context.get(key1) + ", CONSOLE : " + context.get(key2) + ", COMPANY : " + context.getOrDefault("company", "NEC"))
				)
				.publishOn(Schedulers.parallel())
				.contextWrite(Context.of(key1, 3, key2, "PC-FX"));

		mono.subscribe(data -> log.info("# data : {}", data));
		ThreadUtils.sleep(100L);
	}
}
