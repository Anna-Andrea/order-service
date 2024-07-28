package com.github.anna_andrea.order_service.validation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * valid request: { "origin":["40.714224","-73.961452"],
 * "destination":["34.052235","-118.243683"] } InValid request: {
 * "origin":[40.714224,"-73.961452"], "destination":[34.052235,"-118.243683"] }
 * 
 * @author Kevin
 *
 */
public class StrictStringDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if (!p.getCurrentToken().isScalarValue() || p.getCurrentToken().isNumeric()) {
			throw new IOException("Expected string value, but got a number");
		}
		return p.getText();
	}
}
