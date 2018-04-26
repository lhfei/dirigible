/*
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.engine.js.debug.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugModelFacade {

	private static final Logger logger = LoggerFactory.getLogger(DebugModelFacade.class);

	private static DebugModelFacade debugModelFacade;

	public static DebugModelFacade getInstance() {
		if (debugModelFacade == null) {
			debugModelFacade = new DebugModelFacade();
		}
		return debugModelFacade;
	}

	public DebugSessionModel getDebugSessionModel(String user, String executionId) {
		DebugSessionModel debugModel = getDebugModel(user).getSessionByExecutionId(executionId);
		if (debugModel == null) {
			logger.warn("Getting debug session with executionId: " + executionId + " failed - no such session exists");
		}
		return debugModel;
	}

	public void removeSession(String user, String executionId) {
		DebugSessionModel session = getDebugModel(user).getSessionByExecutionId(executionId);
		getDebugModel(user).getSessions().remove(session);
		logger.debug("Debug session with executionId: " + executionId + " removed");
	}

	public static DebugModel getDebugModel(String user) {
		DebugModel debugModel = DebugManager.getDebugModel(user);
		if (debugModel == null) {
			logger.debug("Debug Model has not been created!");
		}
		return debugModel;
	}

	public static DebugModel createDebugModel(String user, IDebugController debugController) {
		DebugModel debugModel = new DebugModel(debugController);
		DebugManager.registerDebugModel(user, debugModel);
		return debugModel;
	}

}
