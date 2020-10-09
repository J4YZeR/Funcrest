package com.testapps.funcrest.controller;

import com.testapps.funcrest.service.implementation.ProcessFuncServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class MainController {
	ProcessFuncServiceImpl service;
	public MainController(ProcessFuncServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/")
	public String hello() {
		return "Hello world!";
	}
	@GetMapping(path = "/calculate_funcs", produces = "application/stream+json")
	public ResponseEntity<Flux<String>> calculateFuncs(@ApiParam("JS function code") @RequestParam("func1") String func1,
													   @ApiParam("JS function code") @RequestParam("func2") String func2,
													   @ApiParam("Number of iterations") @RequestParam("iter_num") int iterNum,
													   @ApiParam("sorted/unsorted") @RequestParam("output_mode") String outputMode) {
		try {
			if(outputMode.equals("sorted")) {
				return ResponseEntity.ok(service.getSortedResults(func1, func2, iterNum));
			}
			else if(outputMode.equals("unsorted")) {
				return ResponseEntity.ok(service.getUnsortedResults(func1, func2, iterNum));
			}
			return new ResponseEntity<Flux<String>>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<Flux<String>>(HttpStatus.BAD_REQUEST);
		}

	}
}
