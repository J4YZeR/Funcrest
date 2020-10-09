package com.testapps.funcrest.service.implementation;

import com.testapps.funcrest.service.ProcessFuncService;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ProcessFuncServiceImpl implements ProcessFuncService {
	@Override
	public Flux<String> getSortedResults(String func1, String func2, int n) {
		AtomicInteger count1 = new AtomicInteger();
		AtomicInteger count2 = new AtomicInteger();
		AtomicInteger i = new AtomicInteger();
		var func1Flux = getFluxForFunc(func1, n, count1);
		var func2Flux = getFluxForFunc(func2, n, count2);
		i.set(1);
		return Flux.zip(func1Flux, func2Flux).map(e ->
				i.getAndIncrement() + ", " + e.getT1() + ", " + count1.decrementAndGet() + ", " + e.getT2() + ", " + count2.decrementAndGet() + "\n");
	}

	@Override
	public Flux<String> getUnsortedResults(String func1, String func2, int n) {
		AtomicInteger count1 = new AtomicInteger();
		AtomicInteger count2 = new AtomicInteger();
		var func1Flux = getFluxForFunc(func1, n, count1).map(e -> count1.get() + ", " + "1, " + e);
		var func2Flux = getFluxForFunc(func2, n, count2).map(e -> count2.get() + ", " + "2, " + e);
		return Flux.merge(func1Flux, func2Flux).map(e -> e + "\n");

	}
	private Flux<String> getFluxForFunc(String funcText, int n, AtomicInteger counter) {
			return Flux.range(1, n).publishOn(Schedulers.elastic()).map(e -> {
						Context poly = Context.create();
						Instant start = Instant.now();
						Value func = poly.eval("js", "(" + funcText + ")");
						Value obj = func.execute(e);
						String funcResult = obj.toString();
						Instant finish = Instant.now();
						counter.incrementAndGet();
						long timeElapsed = Duration.between(start, finish).toMillis();
						return funcResult + ", " + timeElapsed;
					}).onErrorMap(e -> new ResponseStatusException(BAD_REQUEST, e.getMessage()));
	}
}
