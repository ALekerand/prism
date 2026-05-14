package com.dcspa.prism.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class BlockingReactiveTest {

	@Test
	void monoRunsCallableOffCallingThread() {
		Mono<String> mono = BlockingReactive.mono(() -> "ok");
		assertEquals("ok", mono.block());
	}

	@Test
	void monoPropagatesException() {
		Mono<Object> mono = BlockingReactive.mono(() -> {
			throw new IllegalStateException("boom");
		});
		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalStateException.class,
				mono::block);
	}
}
