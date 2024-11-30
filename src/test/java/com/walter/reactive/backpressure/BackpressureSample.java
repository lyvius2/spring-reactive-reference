package com.walter.reactive.backpressure;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureSample {
	public static int count = 0;
	private final static Logger log = LoggerFactory.getLogger(BackpressureSample.class);

	@Test
	@DisplayName("Subscriber가 처리 가능한 만큼의 Request 개수를 제어하는 Backpressure Sample #1")
	public void backpressure_control_maximum_request_test1() {
		Flux.range(1, 5)
				.doOnNext(doOnNext -> log.info("# doOnNext {}", doOnNext))
				.doOnRequest(doOnRequest -> log.info("# doOnRequest {}", doOnRequest))
				.subscribe(new BaseSubscriber<Integer>() {
					@Override
					protected void hookOnSubscribe(Subscription subscription) {
						request(1);
					}

					@Override
					protected void hookOnNext(Integer value) {
						ThreadUtils.sleep(2_000);
						log.info("# OnNext {}", value);
						request(1);
					}
				});
	}

	@Test
	@DisplayName("Subscriber가 처리 가능한 만큼의 Request 개수를 제어하는 Backpressure Sample #1")
	public void backpressure_control_maximum_request_test2() {
		Flux.range(1, 5)
				.doOnNext(doOnNext -> log.info("# doOnNext {}", doOnNext))
				.doOnRequest(doOnRequest -> log.info("# doOnRequest {}", doOnRequest))
				.subscribe(new BaseSubscriber<Integer>() {
					@Override
					protected void hookOnSubscribe(Subscription subscription) {
						request(2);
					}

					@Override
					protected void hookOnNext(Integer value) {
						count++;
						log.info("# OnNext {}", value);
						if (count == 2) {
							ThreadUtils.sleep(2_000);
							request(2);
							count = 0;
						}
					}
				});
	}
}
