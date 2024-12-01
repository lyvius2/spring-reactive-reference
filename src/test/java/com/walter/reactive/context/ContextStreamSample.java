package com.walter.reactive.context;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextStreamSample {
	private static final Logger log = LoggerFactory.getLogger(ContextStreamSample.class);

	@Test
	@DisplayName("Context는 각각의 구독을 통해 Reactor Sequence에 연결되며 chain의 각 연산자가 연결된 Context에 접근할 수 있음")
	public void contextFeatureTest01() {
		final String key1 = "id";
		final Mono<String> mono = Mono.deferContextual(context ->
					Mono.just("ID : " + context.get(key1))
				)
				.publishOn(Schedulers.parallel());

		mono.contextWrite(context -> context.put(key1, "SONY_001"))
				.subscribe(data -> log.info("# data : {}", data));

		mono.contextWrite(context -> context.put(key1, "NINTENDO_002"))
				.subscribe(data -> log.info("# data : {}", data));

		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("Context는 chain의 아래에서 위로 전파된다.")
	public void contextFeatureTest02() {
		final String key1 = "id";
		final String key2 = "console";

		Mono.deferContextual(context -> Mono.just(context.get(key1)))
				.publishOn(Schedulers.parallel())
				.contextWrite(context -> context.put(key2, "Sega Saturn"))
				.transformDeferredContextual(
						(mono, context) -> mono.map(data -> data + ", " + context.getOrDefault(key2, "NEC PC-FX"))
				)
				.contextWrite(context -> context.put(key1, "ETC_001"))
				.subscribe(data -> log.info("# data : {}", data));

		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("동일한 키에 대해서 write하면 가장 마지막에 write한 값이 적용된다.")
	public void contextFeatureTest03() {
		final String key1 = "id";
		final String sampleValue = "SONY_001";

		Mono.deferContextual(context ->
						Mono.just("ID : " + context.get(key1))
				)
				.publishOn(Schedulers.parallel())
				.contextWrite(context -> context.put(key1, sampleValue))
				.contextWrite(context -> context.put(key1, "NINTENDO_002"))
				.subscribe(data -> assertEquals(data, "ID : " + sampleValue));

		ThreadUtils.sleep(100L);
	}

	@Test
	@DisplayName("inner sequence 에서는 외부 context에 저장돤 데이터를 읽을 수 있고, inner sequence 에서 저장한 데이터는 외부 context에 영향을 주지 않는다.")
	public void contextFeatureTest04() {
		final String key1 = "id";
		Mono.just("PlayStation2")
				.flatMap(name -> Mono.deferContextual(ctx ->
						Mono.just(ctx.get(key1) + ", " + name)
								.transformDeferredContextual((mono, innerContext) ->
										mono.map(data -> data + ", " + innerContext.get("console"))
								)
								.contextWrite(context -> context.put("console", "PlayStation5"))
					)
				)
				.publishOn(Schedulers.parallel())
				.contextWrite(context -> context.put(key1, "SONY_001"))
				.subscribe(data -> log.info("# data : {}", data));

		ThreadUtils.sleep(100L);
	}
}
