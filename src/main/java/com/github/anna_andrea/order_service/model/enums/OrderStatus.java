package com.github.anna_andrea.order_service.model.enums;

/**
 * @author Kevin
 *
 */
public enum OrderStatus {
	UNASSIGNED("UNASSIGNED"),
	TAKEN("TAKEN");

	private final String status;
	
	private OrderStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
