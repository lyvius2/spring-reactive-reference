package com.walter.reactive;

import com.walter.reactive.model.Book;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ContextReal {
	public static final String HEADER_NAME_AUTH_TOKEN = "authToken";

	public static void main(String[] args) {
		final Book book = new Book("qwer-1234-5678-8910", "Reactive Programming", "Walter");
		final Mono<String> mono =
				postBook(Mono.just(book))
						.contextWrite(Context.of(HEADER_NAME_AUTH_TOKEN, "eyJhbGciOiJIUzUxMiJ9.eyJzdWI"));
		mono.subscribe(data -> System.out.println("# data : " + data));
	}

	private static Mono<String> postBook(Mono<Book> book) {
		return Mono.zip(book, Mono.deferContextual(context -> Mono.just(context.get(HEADER_NAME_AUTH_TOKEN))))
				.flatMap(tuple -> Mono.just(tuple))
				.flatMap(tuple -> {
					final String response = "POST the book(" + tuple.getT1().getBookName() + ", " + tuple.getT1().getAuthor() + ") with token : " + tuple.getT2();
					return Mono.just(response);
				});
	}
}
