/**
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package de.dlr.sc.virsat.fdir.galileo;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import de.dlr.sc.virsat.fdir.galileo.formatting.DftFormatter;
import org.eclipse.xtext.formatting.IFormatter;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.SimpleLocalScopeProvider;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings("all")
public class DftRuntimeModule extends AbstractDftRuntimeModule {
  @Override
  public Class<? extends IFormatter> bindIFormatter() {
    return DftFormatter.class;
  }
  
  @Override
  public void configureIScopeProviderDelegate(final Binder binder) {
    binder.<IScopeProvider>bind(IScopeProvider.class).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(SimpleLocalScopeProvider.class);
  }
}
