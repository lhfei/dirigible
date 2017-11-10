/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.api.v3.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.commons.api.context.ContextException;
import org.eclipse.dirigible.commons.api.context.InvalidStateException;
import org.eclipse.dirigible.commons.api.context.ThreadContextFacade;
import org.eclipse.dirigible.commons.api.helpers.BytesHelper;
import org.eclipse.dirigible.commons.api.helpers.GsonHelper;
import org.eclipse.dirigible.commons.api.scripting.IScriptingFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java facade for working with HttpServletRequest
 */
public class HttpRequestFacade implements IScriptingFacade {

	/** The Constant ATTRIBUTE_REST_RESOURCE_PATH. */
	public static final String ATTRIBUTE_REST_RESOURCE_PATH = "dirigible-rest-resource-path";
	private static final String NO_VALID_REQUEST = "Trying to use HTTP Request Facade without a valid Request";

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestFacade.class);

	/**
	 * Returns the request in the current thread context
	 *
	 * @return the request
	 */
	static final HttpServletRequest getRequest() {
		if (!ThreadContextFacade.isValid()) {
			return null;
		}
		try {
			return (HttpServletRequest) ThreadContextFacade.get(HttpServletRequest.class.getCanonicalName());
		} catch (ContextException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Checks if there is a request in the current thread context
	 *
	 * @return true, if there is a request in the current thread context
	 */
	public static final boolean isValid() {
		HttpServletRequest request = getRequest();
		return request != null;
	}

	/**
	 * Returns the name of the HTTP method with which this request in the current thread context was made
	 *
	 * @return the HTTP method of the request
	 * @see HttpServletRequest#getMethod()
	 */
	public static final String getMethod() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getMethod();
	}

	/**
	 * Returns the login of the user making the request or null if the user hasn't been authenticated
	 *
	 * @return the login of the user making the request
	 * @see HttpServletRequest#getRemoteUser()
	 */
	public static final String getRemoteUser() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRemoteUser();
	}

	/**
	 * Returns any extra path information associated with the URL the client sent when it made this request
	 *
	 * @return the path info
	 * @see HttpServletRequest#getPathInfo()
	 */
	public static final String getPathInfo() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getPathInfo();
	}

	/**
	 * Returns any extra path information after the servlet name but before the query string, and translates it to a real
	 * path
	 *
	 * @return the path translated
	 * @see HttpServletRequest#getPathTranslated()
	 */
	public static final String getPathTranslated() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getPathTranslated();
	}

	/**
	 * Returns the value of the specified request header as a String. If the request did not include a header of the
	 * specified name, this method returns null
	 *
	 * @param name
	 *            the header name
	 * @return the header value
	 * @see HttpServletRequest#getHeader(String)
	 */
	public static final String getHeader(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getHeader(name);
	}

	/**
	 * Checks if is user in role.
	 *
	 * @param role
	 *            the role
	 * @return true, if is user in role
	 * @see HttpServletRequest#isUserInRole(String)
	 */
	public static final boolean isUserInRole(String role) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.isUserInRole(role);
	}

	/**
	 * Returns the attribute as string
	 *
	 * @param name
	 *            the name
	 * @return the attribute
	 * @see HttpServletRequest#getAttribute(String)
	 */
	public static final String getAttribute(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getAttribute(name) != null ? request.getAttribute(name).toString() : null;
	}

	/**
	 * Returns the auth type.
	 *
	 * @return the auth type
	 * @see HttpServletRequest#getAuthType()
	 */
	public static final String getAuthType() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getAuthType();
	}

	/**
	 * Returns the cookies.
	 *
	 * @return the cookies
	 */
	public static final String getCookies() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return GsonHelper.GSON.toJson(request.getCookies());
	}

	/**
	 * Returns the attribute names.
	 *
	 * @return the attribute names
	 */
	public static final String getAttributeNames() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		List<String> list = Collections.list(request.getAttributeNames());
		return GsonHelper.GSON.toJson(list.toArray());
	}

	/**
	 * Returns the character encoding.
	 *
	 * @return the character encoding
	 */
	public static final String getCharacterEncoding() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getCharacterEncoding();
	}

	/**
	 * Returns the content length.
	 *
	 * @return the content length
	 */
	public static final int getContentLength() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getContentLength();
	}

	/**
	 * Returns the headers.
	 *
	 * @param name
	 *            the name
	 * @return the headers
	 */
	public static final String getHeaders(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		List<String> list = Collections.list(request.getHeaders(name));
		return GsonHelper.GSON.toJson(list.toArray());
	}

	/**
	 * Returns the content type.
	 *
	 * @return the content type
	 */
	public static final String getContentType() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getContentType();
	}

	/**
	 * Returns the bytes.
	 *
	 * @return the bytes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static final String getBytes() throws IOException {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return BytesHelper.bytesToJson(IOUtils.toByteArray(request.getInputStream()));
	}

	/**
	 * Returns the text.
	 *
	 * @return the text
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static final String getText() throws IOException {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		byte[] bytes = IOUtils.toByteArray(request.getInputStream());
		String charset = (request.getCharacterEncoding() != null) ? request.getCharacterEncoding() : StandardCharsets.UTF_8.name();
		return new String(bytes, charset);
	}

	/**
	 * Returns the parameter.
	 *
	 * @param name
	 *            the name
	 * @return the parameter
	 */
	public static final String getParameter(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getParameter(name);
	}

	/**
	 * Returns the parameters.
	 *
	 * @return the parameters
	 */
	public static final String getParameters() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return GsonHelper.GSON.toJson(request.getParameterMap());
	}

	/**
	 * Returns the resource path.
	 *
	 * @return the resource path
	 */
	public static final String getResourcePath() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		Object resourcePathParameter = request.getAttribute(ATTRIBUTE_REST_RESOURCE_PATH);
		return (resourcePathParameter != null ? resourcePathParameter.toString() : "");
	}

	/**
	 * Returns the header names.
	 *
	 * @return the header names
	 */
	public static final String getHeaderNames() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		List<String> list = Collections.list(request.getHeaderNames());
		return GsonHelper.GSON.toJson(list.toArray());
	}

	/**
	 * Returns the parameter names.
	 *
	 * @return the parameter names
	 */
	public static final String getParameterNames() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		List<String> list = Collections.list(request.getParameterNames());
		return GsonHelper.GSON.toJson(list.toArray());
	}

	/**
	 * Returns the parameter values.
	 *
	 * @param name
	 *            the name
	 * @return the parameter values
	 */
	public static final String getParameterValues(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return GsonHelper.GSON.toJson(request.getParameterValues(name));
	}

	/**
	 * Returns the protocol.
	 *
	 * @return the protocol
	 */
	public static final String getProtocol() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getProtocol();
	}

	/**
	 * Returns the scheme.
	 *
	 * @return the scheme
	 */
	public static final String Returnscheme() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getScheme();
	}

	/**
	 * Returns the context path.
	 *
	 * @return the context path
	 */
	public static final String getContextPath() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getContextPath();
	}

	/**
	 * Returns the server name.
	 *
	 * @return the server name
	 */
	public static final String ReturnserverName() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getServerName();
	}

	/**
	 * Returns the server port.
	 *
	 * @return the server port
	 */
	public static final int ReturnserverPort() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getServerPort();
	}

	/**
	 * Returns the query string.
	 *
	 * @return the query string
	 */
	public static final String getQueryString() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getQueryString();
	}

	/**
	 * Returns the remote address.
	 *
	 * @return the remote address
	 */
	public static final String getRemoteAddress() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRemoteAddr();
	}

	/**
	 * Returns the remote host.
	 *
	 * @return the remote host
	 */
	public static final String getRemoteHost() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRemoteHost();
	}

	/**
	 * Sets the attribute.
	 *
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public static final void setAttribute(String name, String value) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		request.setAttribute(name, value);
	}

	/**
	 * Removes the attribute.
	 *
	 * @param name
	 *            the name
	 */
	public static final void removeAttribute(String name) {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		request.removeAttribute(name);
	}

	/**
	 * Returns the locale.
	 *
	 * @return the locale
	 */
	public static final String getLocale() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return GsonHelper.GSON.toJson(request.getLocale());
	}

	/**
	 * Returns the request URI.
	 *
	 * @return the request URI
	 */
	public static final String getRequestURI() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRequestURI();
	}

	/**
	 * Checks if is secure.
	 *
	 * @return true, if is secure
	 */
	public static final boolean isSecure() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.isSecure();
	}

	/**
	 * Returns the request URL.
	 *
	 * @return the request URL
	 */
	public static final String getRequestURL() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRequestURL().toString();
	}

	/**
	 * Returns the service path.
	 *
	 * @return the service path
	 */
	public static final String ReturnservicePath() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getServletPath();
	}

	/**
	 * Returns the remote port.
	 *
	 * @return the remote port
	 */
	public static final int getRemotePort() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getRemotePort();
	}

	/**
	 * Returns the local name.
	 *
	 * @return the local name
	 */
	public static final String getLocalName() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getLocalName();
	}

	/**
	 * Returns the local addr.
	 *
	 * @return the local addr
	 */
	public static final String getLocalAddr() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getLocalAddr();
	}

	/**
	 * Returns the local port.
	 *
	 * @return the local port
	 */
	public static final int getLocalPort() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			throw new InvalidStateException(NO_VALID_REQUEST);
		}
		return request.getLocalPort();
	}

}
