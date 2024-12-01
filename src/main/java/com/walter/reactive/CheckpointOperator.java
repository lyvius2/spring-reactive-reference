package com.walter.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CheckpointOperator {
	public static void main(String[] args) {
		final Flux<Integer> source = Flux.just(2, 4, 6, 8);
		final Flux<Integer> other = Flux.just(1, 2, 3, 0);

		final Flux<Integer> multiplySource = divide(source, other).checkpoint();
		final Flux<Integer> plusSource = plus(multiplySource).checkpoint();

		plusSource.subscribe(
				data -> log.info("# result : {}", data),
				error -> error.printStackTrace()
		);
	}

	private static Flux<Integer> divide(Flux<Integer> source, Flux<Integer> other) {
		return source.zipWith(other, (a, b) -> a / b);
	}

	private static Flux<Integer> plus(Flux<Integer> source) {
		return source.map(num -> num + 2);
	}
}
