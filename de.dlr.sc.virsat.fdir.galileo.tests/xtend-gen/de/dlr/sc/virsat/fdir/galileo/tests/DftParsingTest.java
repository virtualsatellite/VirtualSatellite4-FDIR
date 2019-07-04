/**
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package de.dlr.sc.virsat.fdir.galileo.tests;

import com.google.inject.Inject;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.tests.DftInjectorProvider;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(DftInjectorProvider.class)
@SuppressWarnings("all")
public class DftParsingTest {
  @Inject
  private ParseHelper<GalileoDft> parseHelper;
  
  @Test
  public void loadModel() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("toplevel \"Fault\";");
      _builder.newLine();
      _builder.append("\"Fault\" lambda=0.5 dorm=0.1;");
      _builder.newLine();
      final GalileoDft result = this.parseHelper.parse(_builder);
      Assert.assertNotNull(result);
      Assert.assertEquals("Name correct", "Fault", result.getRoot().getName());
      Assert.assertEquals("Lambda correct", "0.5", result.getBasicEvents().get(0).getLambda());
      Assert.assertEquals("Dormancy correct", "0.1", result.getBasicEvents().get(0).getDorm());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
