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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.serializer.ISerializer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;

/**
 * This class takes care of serializing a given GalileoDFT into the passed file.
 * @author muel_s8
 *
 */

public class GalileoDFTWriter {
	
	private String path;
	
	/**
	 * The path to the target destination
	 * @param path the path
	 */
	public GalileoDFTWriter(String path) {
		this.path = path;
	}
	
	/**
	 * The galileo dft to write
	 * @param galileoDft the galileo dft
	 * @return the created file
	 * @throws IOException 
	 */
	public File write(GalileoDft galileoDft) throws IOException {
		File file = new File(path);
		FileOutputStream outputStream = new FileOutputStream(file);
		ResourceSet set = new ResourceSetImpl(); 
		Resource resource = set.createResource(URI.createURI("tmp")); 
		resource.getContents().add(galileoDft);
		
		Injector injector = Guice.createInjector(new DftRuntimeModule());
		ISerializer serializer = injector.getInstance(ISerializer.class);  
		serializer.serialize(galileoDft, new OutputStreamWriter(outputStream), SaveOptions.newBuilder().format().getOptions());
		
		outputStream.close();
		
		return file;
	}
}
