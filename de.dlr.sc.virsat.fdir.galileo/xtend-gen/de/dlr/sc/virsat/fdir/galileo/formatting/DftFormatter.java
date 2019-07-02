/**
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package de.dlr.sc.virsat.fdir.galileo.formatting;

import com.google.inject.Inject;
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess;
import java.util.List;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.xbase.lib.Extension;

/**
 * This class contains custom formatting declarations.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#formatting
 * on how and when to use it.
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
@SuppressWarnings("all")
public class DftFormatter extends AbstractDeclarativeFormatter {
  @Inject
  @Extension
  private DftGrammarAccess _dftGrammarAccess;
  
  @Override
  protected void configureFormatting(final FormattingConfig c) {
    List<Keyword> _findKeywords = this._dftGrammarAccess.findKeywords(";");
    for (final Keyword comma : _findKeywords) {
      {
        c.setNoLinewrap().before(comma);
        c.setNoSpace().before(comma);
        c.setLinewrap().after(comma);
      }
    }
    List<Keyword> _findKeywords_1 = this._dftGrammarAccess.findKeywords("=");
    for (final Keyword equals : _findKeywords_1) {
      {
        c.setNoLinewrap().before(equals);
        c.setNoSpace().after(equals);
        c.setNoSpace().before(equals);
        c.setLinewrap().after(equals);
      }
    }
    c.setNoLinewrap().before(this._dftGrammarAccess.getGalileoGateAccess().getChildrenAssignment_2());
    c.setNoLinewrap().before(this._dftGrammarAccess.getGalileoNodeTypeRule());
    c.setNoLinewrap().after(this._dftGrammarAccess.getGalileoNodeTypeRule());
  }
}
