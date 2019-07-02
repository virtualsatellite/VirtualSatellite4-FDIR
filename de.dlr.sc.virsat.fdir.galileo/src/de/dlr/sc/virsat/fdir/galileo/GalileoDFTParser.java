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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;

/**
 * Galileo DFT parser
 * @author muel_s8
 *
 */

public class GalileoDFTParser {
    @Inject
    private XtextResourceSet resourceSet;
 
    /**
     * Default Constructor
     */
    
    public GalileoDFTParser() {
        setupParser();
    }
 
    /**
     * Setup method
     */
    private void setupParser() {
		Injector injector = new DftStandaloneSetup().createInjectorAndDoEMFRegistration();
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
    }
 
    /**
     * Parses an input stream and returns the resulting object tree root element.
     * @param in Input Stream
     * @return Root model object
     * @throws IOException When and I/O related parser error occurs
     */
    public GalileoDft parse(InputStream in) throws IOException {
    	Resource resource = resourceSet.createResource(URI.createFileURI("DFT.dft"));
        resource.load(in, resourceSet.getLoadOptions());
        return (GalileoDft) resource.getContents().get(0);
    }
 
    /**
     * Parses a resource specified by an URI and returns the resulting object tree root element.
     * @param uri URI of resource to be parsed
     * @return Root model object
     */
    public GalileoDft parse(URI uri) {
        Resource resource = resourceSet.getResource(uri, true);
        return (GalileoDft) resource.getContents().get(0);
    }
}
