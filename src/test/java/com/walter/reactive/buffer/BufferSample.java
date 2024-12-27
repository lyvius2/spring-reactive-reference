package com.walter.reactive.buffer;

import com.walter.reactive.util.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import javax.swing.plaf.TableHeaderUI;
import java.time.Duration;

public class BufferSample {
	private final static Logger log = LoggerFactory.getLogger(BufferSample.class);

	@Test
	@DisplayName("Buffer Operator 테스트")
	void bufferOperatorTest() {
		Flux.range(1, 95)
				.buffer(10)
				.subscribe(data -> log.info("# data : {}", data));
	}

	@Test
	@DisplayName("BufferTimeout Operator 테스트")
	void bufferTimeoutOperatorTest() {
		Flux.range(1, 20)
				.map(num -> {
					if (num < 10) {
						ThreadUtils.sleep(100L);
					} else {
						ThreadUtils.sleep(300L);
					}
					return num;
				})
				.bufferTimeout(3, Duration.ofMillis(400L))
				.subscribe(data -> log.info("# data : {}", data));
	}
}
