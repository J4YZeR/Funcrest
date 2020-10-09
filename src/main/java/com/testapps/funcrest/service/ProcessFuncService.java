package com.testapps.funcrest.service;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

public interface ProcessFuncService {
	Flux<String> getSortedResults(String func1, String func2, int n) throws Exception;
	Flux<String> getUnsortedResults(String func1, String func2, int n) throws Exception;
}
