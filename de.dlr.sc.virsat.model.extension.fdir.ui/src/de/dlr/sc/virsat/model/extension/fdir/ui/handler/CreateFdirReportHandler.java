/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.handler;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.IHandler;

import de.dlr.sc.virsat.apps.ui.handler.CopyAndOpenAppHandler;
import de.dlr.sc.virsat.model.extension.fdir.Activator;

/**
 * Handler for creating and opening a script that creates an FDIR Report
 * @author muel_s8
 *
 */

public class CreateFdirReportHandler extends CopyAndOpenAppHandler implements IHandler {

	public static final String REPORT_FILE_NAME = "savoir_fdir_report.rpttemplate";
	
	@Override
	protected InputStream getAppStream() throws IOException {
		return Activator.getResourceContentAsString("/resources/birt/" + REPORT_FILE_NAME);
	}

	@Override
	protected String getAppName() {
		return REPORT_FILE_NAME;
	}
}
