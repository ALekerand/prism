package com.dcspa.prism.support;

import java.util.concurrent.Callable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Pont JPA / code bloquant → WebFlux : exécute le callable hors du thread Netty
 * ({@link Schedulers#boundedElastic()}).
 */
public final class BlockingReactive {

	private BlockingReactive() {
	}

	public static <T> Mono<T> mono(Callable<T> callable) {
		return Mono.fromCallable(callable).subscribeOn(Schedulers.boundedElastic());
	}
}
