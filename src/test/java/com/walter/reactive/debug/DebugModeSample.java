package com.walter.reactive.debug;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.HashMap;
import java.util.List;

public class DebugModeSample {
	private final static Logger log = LoggerFactory.getLogger(DebugModeSample.class);

	@Test
	@DisplayName("Non-Debug Mode")
	public void nonDebugMode_01() {
		getUnclearNumberFlux()
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error(ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Debug Mode 활성화")
	public void debugMode_01() {
		Hooks.onOperatorDebug();

		getUnclearNumberFlux()
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error(ExceptionUtils.readStackTrace(error))
				);
	}

	private Flux<Integer> getUnclearNumberFlux() {
		return Flux.just(2, 4, 6, 8)
				.zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y);
	}

	@Test
	@DisplayName("Non-Debug Mode")
	public void nonDebugMod_02() {
		getUnclearFruitsFlux()
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error(ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Debug Mode 활성화")
	public void debugMode_02() {
		Hooks.onOperatorDebug();

		getUnclearFruitsFlux()
				.map(translated -> "맛있는 " + translated)
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error(ExceptionUtils.readStackTrace(error))
				);
	}

	private Flux<String> getUnclearFruitsFlux() {
		final HashMap<String, Object> fruits = new HashMap<>();
		fruits.put("banana", "바나나");
		fruits.put("apple", "사과");
		fruits.put("grape", "포도");
		fruits.put("walter melon", "수박");

		return Flux.fromArray(List.of("BANANAS", "APPLES", "KIWIS", "WALTER MELONS").toArray(new String[]{}))
				.map(String::toLowerCase)
				.map(fruit -> fruit.substring(0, fruit.length() -1))
				.map(fruit -> (String)fruits.get(fruit));
	}
}
