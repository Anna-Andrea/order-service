package com.github.anna_andrea.order_service.model.enums;

/**
 * @author Kevin
 *
 */
public enum DistanceServiceExceptionMsg {
	FAILED_TO_FETCH_DISTANCE("Failed to fetch distance: "), NO_DISTANCE_DATA_AVAILABLE("No distance data available.");

	private final String msg;

	private DistanceServiceExceptionMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
