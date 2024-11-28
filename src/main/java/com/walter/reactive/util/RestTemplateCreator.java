package com.walter.reactive.util;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

public class RestTemplateCreator {
	public static RestTemplate create() {
		try {
			final TrustStrategy trustStrategy = (X509Certificate[] chain, String authType) -> true;
			final SSLContext sslContext = SSLContextBuilder.create()
					.loadTrustMaterial(null, trustStrategy)
					.build();
			final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf)
					.register("http", new PlainConnectionSocketFactory())
					.build();
			final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			final CloseableHttpClient httpClient = HttpClients.custom()
					.setConnectionManager(connectionManager)
					.build();
			final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			return new RestTemplate(requestFactory);
		} catch (Exception e) {
			throw new RuntimeException("Failed to configure RestTemplate", e);
		}
	}
}
