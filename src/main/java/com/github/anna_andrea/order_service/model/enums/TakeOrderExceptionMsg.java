package com.github.anna_andrea.order_service.model.enums;

/**
 * @author Kevin
 *
 */
public enum TakeOrderExceptionMsg {
	ORDER_NOT_FOUND("Order not found."), ORDER_ALREADY_TAKEN("Order already taken.");

	private final String msg;

	private TakeOrderExceptionMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
