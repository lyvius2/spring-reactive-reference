package com.walter.reactive.util;

public class ThreadUtils {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {

		}
	}
}
