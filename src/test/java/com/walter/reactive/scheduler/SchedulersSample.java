package com.walter.reactive.scheduler;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class SchedulersSample {
	private final static Logger log = LoggerFactory.getLogger(SchedulersSample.class);

	@Test
	@DisplayName("Scheduler Immediate 테스트")
	public void immediateTest() {
		Flux.fromArray(List.of("PlatStation", "GameCube", "DreamCast", "Famicom").toArray())
				.publishOn(Schedulers.parallel())
				.filter(data -> ((String)data).length() < 10)
				.doOnNext(data -> log.info("# filter : {}", data))
				.publishOn(Schedulers.immediate())
				.map(data -> ((String)data).toUpperCase())
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(200L);
	}

	@Test
	@DisplayName("Scheduler Single 테스트 (단일 Thread 하나만 사용한다)")
	public void singleTest() {
		doTask1("task1")
				.subscribe(data -> log.info("# task1 onNext : {}", data));
		doTask1("task2")
				.subscribe(data -> log.info("# task2 onNext : {}", data));
		ThreadUtils.sleep(200L);
	}

	private Flux<String> doTask1(String taskName) {
		return Flux.fromArray(List.of("영국:Europe", "프랑스:Europe", "아이슬란드:Europe", "대한민국:Asia").toArray(new String[]{}))
				.publishOn(Schedulers.single())
				.filter(data -> data.contains(":Europe"))
				.doOnNext(data -> log.info("# {} filter : {}", taskName, data))
				.map(data -> data.substring(0, data.indexOf(":Europe")))
				.doOnNext(data -> log.info("# {} map : {}", taskName, data));
	}

	@Test
	@DisplayName("Scheduler New Single 테스트 (실행될 때마다 새로운 Thread를 생성한다)")
	public void newSingleTest() {
		doTask2("task1")
				.subscribe(data -> log.info("# task1 onNext : {}", data));
		doTask2("task2")
				.subscribe(data -> log.info("# task2 onNext : {}", data));
		ThreadUtils.sleep(200L);
	}

	private Flux<String> doTask2(String taskName) {
		return Flux.fromArray(List.of("영국:Europe", "프랑스:Europe", "아이슬란드:Europe", "대한민국:Asia").toArray(new String[]{}))
				.publishOn(Schedulers.newSingle("new-single", true))
				.filter(data -> data.contains(":Europe"))
				.doOnNext(data -> log.info("# {} filter : {}", taskName, data))
				.map(data -> data.substring(0, data.indexOf(":Europe")))
				.doOnNext(data -> log.info("# {} map : {}", taskName, data));
	}

	@Test
	@DisplayName("Schedulers newBoundedElastic 테스트")
	public void newBoundedElasticTest() {
		final Scheduler scheduler = Schedulers.newBoundedElastic(2, 2, "I/O-Thread");
		final Mono<String> mono = Mono.just("FF3")
				.subscribeOn(scheduler);

		log.info("# Start");

		mono.subscribe(data -> {
			log.info("subscribe 1 doing");
			ThreadUtils.sleep(3_000L);
			log.info("subscribe 1 done : {}", data);
		});

		mono.subscribe(data -> {
			log.info("subscribe 2 doing");
			ThreadUtils.sleep(3_000L);
			log.info("subscribe 2 done : {}", data);
		});

		mono.subscribe(data -> {
			log.info("subscribe 3 doing : {}", data);
		});

		mono.subscribe(data -> {
			log.info("subscribe 4 doing : {}", data);
		});

		mono.subscribe(data -> {
			log.info("subscribe 5 doing : {}", data);
		});

		mono.subscribe(data -> {
			log.info("subscribe 6 doing : {}", data);
		});

		ThreadUtils.sleep(4_000L);
		scheduler.dispose();
	}

	@Test
	@DisplayName("Schedulers newParallel 테스트")
	public void newParallelTest() {
		final Mono<String> flux = Mono.just("DQ4")
				.publishOn(Schedulers.newParallel("Parallel Thread", 4, true));

		flux.subscribe(data -> {
			ThreadUtils.sleep(5_000L);
			log.info("subscribe 1 : {}", data);
		});

		flux.subscribe(data -> {
			ThreadUtils.sleep(4_000L);
			log.info("subscribe 2 : {}", data);
		});

		flux.subscribe(data -> {
			ThreadUtils.sleep(3_000L);
			log.info("subscribe 3 : {}", data);
		});

		flux.subscribe(data -> {
			ThreadUtils.sleep(2_000L);
			log.info("subscribe 4 : {}", data);
		});

		ThreadUtils.sleep(6_000L);
	}
}
