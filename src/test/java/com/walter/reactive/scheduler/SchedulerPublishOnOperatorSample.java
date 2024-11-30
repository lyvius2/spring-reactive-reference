package com.walter.reactive.scheduler;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerPublishOnOperatorSample {
	private static final Logger log = LoggerFactory.getLogger(SchedulerPublishOnOperatorSample.class);

	@Test
	@DisplayName("Scheduler Operator 테스트")
	public void schedulerOperatorTest() {
		Flux.fromArray(new Integer[]{1, 3, 5, 7})
				.filter(data -> data > 3)
				.map(data -> data * 10)
				.subscribe(data -> log.info("# data : {}", data));
	}

	@Test
	@DisplayName("Scheduler Operator PublishOn 테스트 1")
	public void schedulerOperatorPublishOnTest_01() {
		Flux.fromArray(new Integer[]{1, 3, 5, 7})
				.doOnNext(data -> log.info("# fromArray : {}", data))
				.publishOn(Schedulers.parallel())
				.filter(data -> data > 3)
				.doOnNext(data -> log.info("# filter : {}", data))
				.map(data -> data * 10)
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(500L);
	}

	@Test
	@DisplayName("Scheduler Operator PublishOn 테스트 2")
	public void schedulerOperatorPublishOnTest_02() {
		Flux.fromArray(new Integer[]{1, 3, 5, 7})
				.doOnNext(data -> log.info("# fromArray : {}", data))
				.publishOn(Schedulers.parallel())
				.filter(data -> data > 3)
				.doOnNext(data -> log.info("# filter : {}", data))
				.publishOn(Schedulers.parallel())
				.map(data -> data * 10)
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(500L);
	}
}
