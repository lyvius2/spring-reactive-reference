package com.walter.reactive.scheduler;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerSubscriberOnOperatorSample {
	private final static Logger log = LoggerFactory.getLogger(SchedulerSubscriberOnOperatorSample.class);

	@Test
	@DisplayName("Scheduler SubscriberOn Operator 테스트")
	public void subscriberOnOperatorTest_01() {
		Flux.fromArray(new Integer[] {1, 3, 5, 7})
				.subscribeOn(Schedulers.boundedElastic())
				.doOnNext(data -> log.info("# fromArray : {}", data))
				.filter(data -> data > 3)
				.doOnNext(data -> log.info("# filter : {}", data))
				.map(data -> data * 10)
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(500L);
	}

	@Test
	@DisplayName("Scheduler SubscriberOn And PublishOn Operator 테스트")
	public void subscribeOnAndPublishOnOperatorTest() {
		Flux.fromArray(new Integer[] {1, 3, 5, 7})
				.subscribeOn(Schedulers.boundedElastic())
				.filter(data -> data > 3)
				.doOnNext(data -> log.info("# filter : {}", data))
				.publishOn(Schedulers.parallel())
				.map(data -> data * 10)
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(500L);
	}

	@Test
	@DisplayName("Scheduler PublishOn And SubscriberOn Operator 테스트")
	public void publishOnAndSubscribeOnOperatorTest() {
		Flux.fromArray(new Integer[] {1, 3, 5, 7})
				.doOnNext(data -> log.info("# fromArray : {}", data))
				.publishOn(Schedulers.parallel())
				.filter(data -> data > 3)
				.doOnNext(data -> log.info("# filter : {}", data))
				.subscribeOn(Schedulers.boundedElastic())
				.map(data -> data * 10)
				.doOnNext(data -> log.info("# map : {}", data))
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(500L);
	}
}
