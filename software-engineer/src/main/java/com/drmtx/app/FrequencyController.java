package com.drmtx.app;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class FrequencyController {

	private static final Logger _logger = LogManager
			.getLogger(FrequencyController.class);

	@Autowired
	private FrequencyService service;

	@ExceptionHandler(FrequencyNotFoundException.class)
	public ResponseEntity<ErrorDetail> ErrorInfo(HttpServletRequest request,
			Exception exception) {
		ErrorDetail error = new ErrorDetail();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		error.setUrl(request.getRequestURL().append("/error/111").toString());
		return new ResponseEntity<ErrorDetail>(error, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/frequency/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<FrequencyResource>> getFrequency(
			@PathVariable String id,
			@RequestParam(value = "count", defaultValue = "2") String count) {


		long lid = Long.parseLong(id);
		int counter = Integer.parseInt(count);

		return new ResponseEntity<List<FrequencyResource>>(service.getFrequencyResourceById(lid, counter),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/frequency/new", method = RequestMethod.POST,
							produces = MediaType.APPLICATION_JSON_VALUE,
							consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> newFrequency(
			@Validated @RequestBody String inputUrl) {

		RestTemplate restTemplate = new RestTemplate();

		try {
			URI url = new URI(inputUrl.trim());

			ObjectMapper mapperObject = new ObjectMapper();
			List<Object> jsonObjectList = restTemplate.getForObject(url,
					ArrayList.class);

			StringBuilder commentsBodyBuilder = new StringBuilder();

			for (Iterator iterator = jsonObjectList.iterator(); iterator
					.hasNext();) {
				HashMap<String, Object> rootObjectMap = (HashMap<String, Object>) iterator
						.next();

				parseJsonData(rootObjectMap, commentsBodyBuilder);

			}
			
			FrequencyEntity freqObj = service.createFrequencyResource(commentsBodyBuilder.toString());

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(freqObj.getId()).toUri());
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		return null;

	}

	private void parseJsonData(HashMap<String, Object> rootObjectMap,
			StringBuilder commentsBodyBuilder) {

		HashMap<String, Object> dataMap = (HashMap<String, Object>) rootObjectMap
				.get("data");

		List<HashMap<String, Object>> childernArray = (List<HashMap<String, Object>>) dataMap
				.get("children");

		for (HashMap<String, Object> childernData : childernArray) {

			HashMap<String, Object> childDataMap = (HashMap<String, Object>) childernData
					.get("data");
			String bodyDataString = (String) childDataMap.get("body");
			if (bodyDataString != null) {
				commentsBodyBuilder.append(bodyDataString);
				commentsBodyBuilder.append(" ");
			}

			Object repliesData = (Object) childDataMap.get("replies");

			if (repliesData != null && !(repliesData instanceof String)) {
				parseJsonData((HashMap<String, Object>) repliesData,
						commentsBodyBuilder);
			}
		}

	}

}
