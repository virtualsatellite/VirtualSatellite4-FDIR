/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.galileo.dft.DftFactory;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.Named;

/**
 * This class tests the GalileoDFTWriter
 * @author muel_s8
 *
 */
public class GalileoDFTWriterTest {

	@Test
	public void testWrite() throws IOException {
		GalileoDft galileoDft = DftFactory.eINSTANCE.createGalileoDft();
		Named type = DftFactory.eINSTANCE.createNamed();
		type.setTypeName("or");
		GalileoFaultTreeNode root = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
		root.setType(type);
		root.setName("root");
		galileoDft.getGates().add(root);
		galileoDft.setRoot(root);
		
		GalileoFaultTreeNode be = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
		be.setName("be");
		be.setLambda("1.0");
		galileoDft.getBasicEvents().add(be);
		root.getChildren().add(be);
		
		GalileoDFTWriter dftWriter = new GalileoDFTWriter("test");
		
		File file = dftWriter.write(galileoDft);
		file.deleteOnExit();
		
		assertTrue(file.exists());
	}

}
