package com.walter.reactive;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.walter.reactive.util.RestTemplateCreator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

public class MonoTest01 {
	private static final Logger log = LoggerFactory.getLogger(MonoTest01.class);

	@Test
	public void emptyMonoTest() {
		Mono.empty()
				.subscribe(
						data -> log.info("# emitted data : {}", data),
						error -> {},
						() -> log.info("# emitted onComplete signal")
				);
	}

	@Test
	public void restMonoTest() {
		final URI worldTimeUrl = UriComponentsBuilder.newInstance().scheme("https")
				.host("timeapi.io")
				.port(443)
				.path("/api/timezone/zone")
				.queryParam("timeZone", "Asia/Seoul")
				.build()
				.encode()
				.toUri();

		final RestTemplate restTemplate = RestTemplateCreator.create();
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		Mono.just(
				restTemplate.exchange(worldTimeUrl, HttpMethod.GET, new HttpEntity<String>(headers), String.class)
		)
				.map(response -> {
					final DocumentContext jsonContext = JsonPath.parse(response.getBody());
					return jsonContext.<String>read("$.currentLocalTime");
				})
				.subscribe(
						data -> log.info("# emitted data : {}", data),
						error -> log.error(String.valueOf(error)),
						() -> log.info("# emitted onComplete signal")
				);
	}
}
