package com.expense_tracker.password.util;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

	private UrlUtils() {
		throw new AssertionError("This class cannot be instanciate.");
	}

	public static String applicationUrl(HttpServletRequest request) {

		// TODO: handle prod url
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
