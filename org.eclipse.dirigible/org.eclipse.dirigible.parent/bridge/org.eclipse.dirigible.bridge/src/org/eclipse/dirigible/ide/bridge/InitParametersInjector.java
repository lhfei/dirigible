/*******************************************************************************
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.ide.bridge;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitParametersInjector implements IInjector {

	private static final String INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SERVLET_CONFIGURATION_NAME_S_VALUE_S = "Initial Parameter per Request retreived from the Servlet Configuration: name=%s value=%s";

	private static final String INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SYSTEM_ENVIRONMENT_NAME_S_VALUE_S = "Initial Parameter per Request retreived from the System Environment: name=%s value=%s";

	private static final String INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SYSTEM_PROPERTIES_NAME_S_VALUE_S = "Initial Parameter per Request retreived from the System Properties: name=%s value=%s";

	private static final String INITIAL_PARAMETER_SET_TO_THE_ENVIRONMENT_NAME_S_VALUE_S = "Initial Parameter set to the Environment: name=%s value=%s";

	private static final String INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_ENVIRONMENT_NAME_S_VALUE_S = "Initial Parameter exists in the System Environment: name=%s value=%s";

	private static final String INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_PROPERTIES_NAME_S_VALUE_S = "Initial Parameter exists in the System Properties: name=%s value=%s";

	private static final Logger logger = LoggerFactory.getLogger(InitParametersInjector.class.getCanonicalName());

	// Initi Parameters Names
	public static final String INIT_PARAM_RUNTIME_URL = "runtimeUrl"; //$NON-NLS-1$
	public static final String INIT_PARAM_SERVICES_URL = "servicesUrl"; //$NON-NLS-1$
	public static final String INIT_PARAM_ENABLE_ROLES = "enableRoles"; //$NON-NLS-1$
	public static final String INIT_PARAM_LOG_IN_SYSTEM_OUTPUT = "logInSystemOutput"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_DEFAULT_DATASOURCE = "jndiDefaultDataSource"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CONNECTIVITY_CONFIGURATION = "jndiConnectivityService"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION = "jndiCmisService"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_AUTH = "jndiCmisServiceAuth"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_AUTH_KEY = "key"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_AUTH_DEST = "destination"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_NAME = "jndiCmisServiceName"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_KEY = "jndiCmisServiceKey"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_CMIS_CONFIGURATION_DESTINATION = "jndiCmisServiceDestination"; //$NON-NLS-1$
	public static final String INIT_PARAM_JNDI_MAIL_SESSION = "jndiMailService"; //$NON-NLS-1$
	public static final String INIT_PARAM_JDBC_SET_AUTO_COMMIT = "jdbcAutoCommit"; //$NON-NLS-1$
	public static final String INIT_PARAM_JDBC_MAX_CONNECTIONS_COUNT = "jdbcMaxConnectionsCount";
	public static final String INIT_PARAM_JDBC_WAIT_TIMEOUT = "jdbcWaitTimeout";
	public static final String INIT_PARAM_JDBC_WAIT_COUNT = "jdbcWaitCount";
	public static final String INIT_PARAM_REPOSITORY_PROVIDER = "repositoryProvider"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_LOCAL = "local"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_DB = "db"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_MASTER = "repositoryProviderMaster"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_MASTER_DB = "db"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_MASTER_FILESYSTEM = "filesystem"; //$NON-NLS-1$
	public static final String INIT_PARAM_REPOSITORY_PROVIDER_MASTER_GIT = "git"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_DATASOURCE_TYPE = "defaultDataSourceType"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_DATASOURCE_TYPE_JNDI = "jndi"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_DATASOURCE_TYPE_LOCAL = "local"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_MAIL_SERVICE = "mailSender"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_MAIL_SERVICE_PROVIDED = "provided"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_MAIL_SERVICE_BUILTIN = "built-in"; //$NON-NLS-1$
	public static final String INIT_PARAM_HOME_URL = "homeLocation"; //$NON-NLS-1$
	public static final String INIT_PARAM_RUN_TESTS_ON_INIT = "runTestsOnInit"; //$NON-NLS-1$
	public static final String INIT_PARAM_AUTO_ACTIVATE_ENABLED = "autoActivateEnabled"; //$NON-NLS-1$
	public static final String INIT_PARAM_AUTO_PUBLISH_ENABLED = "autoPublishEnabled"; //$NON-NLS-1$
	public static final String INIT_PARAM_ENABLE_SANDBOX = "enableSandbox"; //$NON-NLS-1$
	public static final String INIT_PARAM_LOCAL_REPOSITORY_ROOT_FOLDER = "localRepositoryRootFolder"; //$NON-NLS-1$
	public static final String INIT_PARAM_LOCAL_REPOSITORY_ROOT_FOLDER_IS_ABSOLUTE = "localRepositoryRootFolderIsAbsolute"; //$NON-NLS-1$
	public static final String INIT_PARAM_RUN_ON_OSGI = "osgi"; //$NON-NLS-1$ // true/false
	public static final String INIT_PARAM_LOCAL_DATABASE_ROOT_FOLDER = "localDatabaseRootFolder"; //$NON-NLS-1$
	public static final String INIT_PARAM_LOCAL_CMIS_ROOT_FOLDER = "localCmisRootFolder"; //$NON-NLS-1$
	public static final String INIT_PARAM_DEFAULT_THEME = "defaultTheme"; //$NON-NLS-1$

	// ---

	@Override
	public void injectOnRequest(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String parameterValue = null;
			if (System.getProperties().containsKey(parameterName)) {
				parameterValue = System.getProperty(parameterName);
				logger.debug(String.format(INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SYSTEM_PROPERTIES_NAME_S_VALUE_S, parameterName,
						parameterValue));
			} else if (System.getenv().containsKey(parameterName)) {
				parameterValue = System.getenv().get(parameterName);
				logger.debug(String.format(INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SYSTEM_ENVIRONMENT_NAME_S_VALUE_S, parameterName,
						parameterValue));
			} else {
				parameterValue = servletConfig.getInitParameter(parameterName);
				logger.debug(String.format(INITIAL_PARAMETER_PER_REQUEST_RETREIVED_FROM_THE_SERVLET_CONFIGURATION_NAME_S_VALUE_S, parameterName,
						parameterValue));
			}
			req.setAttribute(parameterName, parameterValue);
		}
	}

	@Override
	public void injectOnStart(ServletConfig servletConfig) throws ServletException, IOException {

		Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String parameterValue = null;
			if (System.getProperties().containsKey(parameterName)) {
				parameterValue = System.getProperty(parameterName);
				logger.info(String.format(INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_PROPERTIES_NAME_S_VALUE_S, parameterName, parameterValue));
				System.out.println(String.format(INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_PROPERTIES_NAME_S_VALUE_S, parameterName, parameterValue));
			}
			if (System.getenv().containsKey(parameterName)) {
				parameterValue = System.getenv().get(parameterName);
				logger.debug(String.format(INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_ENVIRONMENT_NAME_S_VALUE_S, parameterName, parameterValue));
				System.out.println(String.format(INITIAL_PARAMETER_EXISTS_IN_THE_SYSTEM_ENVIRONMENT_NAME_S_VALUE_S, parameterName, parameterValue));
			} else {
				parameterValue = servletConfig.getInitParameter(parameterName);
				System.getProperties().put(parameterName, parameterValue);
				logger.info(String.format(INITIAL_PARAMETER_SET_TO_THE_ENVIRONMENT_NAME_S_VALUE_S, parameterName, parameterValue));
				System.out.println(String.format(INITIAL_PARAMETER_SET_TO_THE_ENVIRONMENT_NAME_S_VALUE_S, parameterName, parameterValue));
			}
		}
	}

	public static String get(String key) {
		String value = System.getProperty(key);
		if (value != null) {
			return value;
		}
		return System.getenv().get(key);
	}

}