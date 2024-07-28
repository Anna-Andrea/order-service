package com.github.anna_andrea.order_service.model.enums;

/**
 * @author Kevin
 *
 */
public enum TakeOrderStatus {
	SUCCESS("SUCCESS");

	private final String status;
	
	private TakeOrderStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
