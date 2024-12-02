package com.walter.reactive.debug;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogOperatorSample {
	private static final Logger log = LoggerFactory.getLogger(LogOperatorSample.class);
	private static Map<String, String> fruits = new HashMap<>();

	static {
		fruits.put("apple", "사과");
		fruits.put("banana", "바나나");
		fruits.put("grape", "포도");
		fruits.put("water melon", "수박");
	}

	@Test
	@DisplayName("Log Operator 테스트 #1")
	public void logOperatorTest_01() {
		Flux.fromArray(List.of("BANANAS", "APPLES", "GRAPES", "MELONS").toArray(new String[]{}))
				.log()
				.map(String::toLowerCase)
				.map(fruit -> fruit.substring(0, fruit.length() - 1))
				.map(fruits::get)
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Log Operator 테스트 #2")
	public void logOperatorTest_02() {
		Hooks.onOperatorDebug();

		Flux.fromArray(List.of("BANANAS", "APPLES", "GRAPES", "MELONS").toArray(new String[]{}))
				.log()
				.map(String::toLowerCase)
				.log()
				.map(fruit -> fruit.substring(0, fruit.length() - 1))
				.log()
				.map(fruits::get)
				.log()
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);
	}

	@Test
	@DisplayName("Log Operator 테스트 #3")
	public void logOperatorTest_03() {
		Hooks.onOperatorDebug();

		Flux.fromArray(List.of("BANANAS", "APPLES", "GRAPES", "MELONS").toArray(new String[]{}))
				.subscribeOn(Schedulers.boundedElastic())
				.log("Fruit.Source")
				.publishOn(Schedulers.parallel())
				.map(String::toLowerCase)
				.log("Fruit.Lower")
				.map(fruit -> fruit.substring(0, fruit.length() - 1))
				.log("Fruit.Substring")
				.map(fruits::get)
				.log("Fruit.Name")
				.subscribe(
						data -> log.info("# result : {}", data),
						error -> log.error("# error : {}", ExceptionUtils.readStackTrace(error))
				);

		ThreadUtils.sleep(100L);
	}
}
