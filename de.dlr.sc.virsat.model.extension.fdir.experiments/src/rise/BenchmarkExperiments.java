/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package rise;



import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
public class BenchmarkExperiments extends ASynthesizerExperiment {
	
	protected BasicSynthesizer synthesizer;
	
	@Before
	public void setUp() {
		super.setUp();
		synthesizer = new BasicSynthesizer();
	}

	/* 		No. BEs:	9
	 * 		No. Gates:	6         */
	@Test
	public void testRC11() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-1-1-sc.dft");
		synthesizer.synthesize(fault);
	}
	
	/* 		No. BEs:	24
	 * 		No. Gates:	18        */
	@Test
	public void testRC12() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-1-2-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	/* 		No. BEs:	54
	 * 		No. Gates:	25        */
	@Test
	public void testRC101() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-10-1-sc.dft");
		synthesizer.synthesize(fault);
	}
	
	/* 		No. BEs:	91
	 * 		No. Gates:	43        */
	@Test
	public void testRC151() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-15-1-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	
	/* 		No. BEs:	116
	 * 		No. Gates:	53        */
	@Test
	public void testRC201() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-20-1-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	
	/* 		No. BEs:	173
	 * 		No. Gates:	111       
	@Test
	public void testRC2020() throws Exception {
		Fault fault = createDFT("/resources/rise2019/rc-20-20-hc.dft");
		synthesizer.synthesize(fault);
	} */
	
	/* 		No. BEs:	213
	 * 		No. Gates:	136       
	@Test
	public void testBigTree213() throws Exception {
		Fault fault = createDFT("/resources/rise2019/bigTree_213BEs.dft");
		synthesizer.synthesize(fault);
	} */

	
	/* 		No. BEs:	253
	 * 		No. Gates:	161       
	@Test
	public void testBigTree253() throws Exception {
		Fault fault = createDFT("/resources/rise2019/bigTree_253BEs.dft");
		synthesizer.synthesize(fault);
	} */
	
}
