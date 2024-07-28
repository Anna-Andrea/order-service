package com.github.anna_andrea.order_service.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.anna_andrea.order_service.exception.DistanceServiceException;
import com.github.anna_andrea.order_service.model.enums.DistanceServiceExceptionMsg;

/**
 * @author Kevin
 *
 */
@Component
public class DistanceService {
	@Value("${google.maps.api.key}")
	private String apiKey;

	@Value("${google.maps.api.url}")
	private String apiUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * getDistanceInMeters
	 * 
	 * @param startLatitude
	 * @param startLongitude
	 * @param endLatitude
	 * @param endLongitude
	 * @return
	 */
	public int getDistanceInMeters(BigDecimal startLatitude, BigDecimal startLongitude, BigDecimal endLatitude,
			BigDecimal endLongitude) {
		// Build the URL dynamically
		String url = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("units", "metric")
				.queryParam("origins", "{startLatitude},{startLongitude}")
				.queryParam("destinations", "{endLatitude},{endLongitude}").queryParam("key", apiKey)
				.buildAndExpand(startLatitude.toPlainString(), startLongitude.toPlainString(),
						endLatitude.toPlainString(), endLongitude.toPlainString())
				.toUriString();

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new DistanceServiceException(DistanceServiceExceptionMsg.FAILED_TO_FETCH_DISTANCE.getMsg(),
						response.getStatusCode());
			}

			JsonNode jsonResponse = objectMapper.readTree(response.getBody());

			if (jsonResponse.get("rows").size() == 0 || jsonResponse.get("rows").get(0).get("elements").size() == 0) {
				throw new DistanceServiceException(DistanceServiceExceptionMsg.NO_DISTANCE_DATA_AVAILABLE.getMsg(),
						response.getStatusCode());
			}

			JsonNode distanceNode = jsonResponse.get("rows").get(0).get("elements").get(0).get("distance");

			return distanceNode.get("value").asInt(); // Distance in meters
		} catch (Exception e) {
			throw new DistanceServiceException(e.getMessage(), e, HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
