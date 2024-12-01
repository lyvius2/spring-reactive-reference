package com.walter.reactive.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
	private String isbn;
	private String bookName;
	private String author;

	public Book(String isbn, String bookName, String author) {
		this.isbn = isbn;
		this.bookName = bookName;
		this.author = author;
	}
}
