package com.walter.reactive.sinks;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class ProgrammaticSample {
	private final static Logger log = LoggerFactory.getLogger(ProgrammaticSample.class);

	@Test
	@DisplayName("create() Operator를 사용한 EmitResult 예제")
	public void create_operator_test() {
		int tasks = 7;
		Flux.create((FluxSink<String> sink) -> {
			IntStream.range(1, tasks)
					 .forEach(num -> sink.next(doTask(num)));
			})
			.subscribeOn(Schedulers.boundedElastic())
			.doOnNext(num -> log.info("# create() : {}", num))
			.publishOn(Schedulers.parallel())
			.map(result -> result + " success!")
			.doOnNext(num -> log.info("# map() : {}", num))
			.publishOn(Schedulers.parallel())
			.subscribe(data -> log.info("# onNext : {}", data));
		ThreadUtils.sleep(500L);
	}

	private static String doTask(int taskNumber) {
		return "Task " + taskNumber + " result";
	}

	@Test
	@DisplayName("Sinks를 사용한 EmitResult 예제")
	public void sinks_test() {
		int tasks = 7;

		Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
		Flux<String> fluxView = unicastSink.asFlux();
		IntStream.range(1, tasks)
				.forEach(num -> {
					new Thread(() -> {
						unicastSink.emitNext(doTask(num), Sinks.EmitFailureHandler.FAIL_FAST);
						log.info("# emitted : {}", num);
					}).start();
					ThreadUtils.sleep(100L);
				});
		fluxView.publishOn(Schedulers.parallel())
				.map(result -> result + " success!")
				.doOnNext(num -> log.info("# map() : {}", num))
				.publishOn(Schedulers.parallel())
				.subscribe(data -> log.info("# onNext : {}", data));

		ThreadUtils.sleep(200L);
	}
}
