package com.testapps.funcrest.service.implementation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProcessFuncServiceImplTest {
	@Autowired
	private ProcessFuncServiceImpl service;
	private static final int n = 2;
	private static final String func1Text =
			"function function1(number) {" +
			" return number * number" +
			"}";
	private static final String func2Text =
			"function function2(number) {" +
			" return number + 1" +
			"}";
	@Test
	void getSortedResults() {
		assertEquals(n * 2, service.getUnsortedResults(func1Text, func2Text, n).count().block());
	}

	@Test
	void getUnsortedResults() {
		assertEquals(n, service.getSortedResults(func1Text, func2Text, n).count().block());
	}
}