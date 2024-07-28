package com.github.anna_andrea.order_service.model.qo;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.github.anna_andrea.order_service.validation.ValidLatitudeLongitude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kevin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderQo {

	/**
	 * "origin": ["START_LATITUDE", "START_LONGITUDE"]
	 */
    @NotNull(message = "Origin must not be null")
    @Size(min = 2, max = 2, message = "Origin must contain exactly 2 elements")
    @ValidLatitudeLongitude
	private List<String> origin;

	/**
	 * "destination": ["END_LATITUDE", "END_LONGITUDE"]
	 */
    @NotNull(message = "Destination must not be null")
    @Size(min = 2, max = 2, message = "Destination must contain exactly 2 elements")
    @ValidLatitudeLongitude
	private List<String> destination;
}
