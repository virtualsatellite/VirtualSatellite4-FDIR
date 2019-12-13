/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.snippet;

import java.awt.Color;
import java.awt.Font;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.dlr.sc.virsat.commons.ui.jface.viewer.ISeriesXYValueLabelProvider;
import de.dlr.sc.virsat.commons.ui.jface.viewer.XYSplineChartViewer;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.AvailabilityAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.project.ui.contentProvider.VirSatFilteredWrappedTreeContentProvider;
import de.dlr.sc.virsat.project.ui.contentProvider.VirSatTransactionalAdapterFactoryContentProvider;
import de.dlr.sc.virsat.project.ui.labelProvider.VirSatTransactionalAdapterFactoryLabelProvider;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetTableAvailabilityAnalysisAvailabilityCurve extends AUiSnippetTableAvailabilityAnalysisAvailabilityCurve implements IUiSnippet {
	private static final int FONT_SIZE = 12;
	private JFreeChart chart;
	private ChartComposite chartComposite;
	private XYSplineChartViewer xyPlotChartViewer;

	private XYSeriesCollection dataset;

	@Override
	protected void setUpTableViewer(EditingDomain editingDomain, FormToolkit toolkit) {
		chart = createChart();
		chartComposite = new ChartComposite(sectionBody, SWT.NONE, chart, true);
		GridData chartLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
		chartLayout.verticalSpan = 2;
		chartComposite.setLayoutData(chartLayout);

		xyPlotChartViewer = new XYSplineChartViewer(dataset, chartComposite);
		xyPlotChartViewer.setContentProvider(new VirSatFilteredWrappedTreeContentProvider(
				new VirSatTransactionalAdapterFactoryContentProvider(adapterFactory).setUpdateCaContainerLabels(true)) {
			@Override
			public Object[] getElements(Object inputElement) {
				AvailabilityAnalysis availAnalysis = new AvailabilityAnalysis((CategoryAssignment) inputElement);
				return new Object[] { availAnalysis };
			}
		});

		xyPlotChartViewer.setLabelProvider(new AvailabilityChartLabelProvider(adapterFactory));
		xyPlotChartViewer.setInput(model);
		
		super.setUpTableViewer(editingDomain, toolkit);
	}
	
	@Override
	public Composite createSectionBody(FormToolkit toolkit, String sectionHeading, String sectionDescription,
			int numberColumns) {
		// Add an additional column for the plot
		Composite composite = super.createSectionBody(toolkit, sectionHeading, sectionDescription, numberColumns + 1);
		return composite;
	}
	
	/**
	 * 
	 * @return chartViewer
	 */
	public XYSplineChartViewer getXyPlotChartViewer() {
		return xyPlotChartViewer;
	}

	private static final double TO_PERCENT = 100;

	/**
	 * Creates the line chart
	 * 
	 * @return a configured line chart using the passed data set
	 */
	private JFreeChart createChart() {
		AvailabilityAnalysis availabilityAnalysis = new AvailabilityAnalysis((CategoryAssignment) model);
		String missionTimeUnit = availabilityAnalysis.getRemainingMissionTimeBean().getTypeInstance().getUnit()
				.getSymbol();

		dataset = new XYSeriesCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(null, "Mission Time [" + missionTimeUnit + "]",
				"Availability [%]", dataset);

		XYPlot plot = chart.getXYPlot();
		plot.setNoDataMessage("No data available");
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.GRAY);
		chart.setAntiAlias(true);
		chart.setBorderVisible(false);
		chart.setBackgroundPaint(Color.white);
		plot.getRangeAxis().setRange(0, TO_PERCENT);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
		renderer.setSeriesVisibleInLegend(0, false);
		renderer.setBaseShapesVisible(false);

		// set the title
		TextTitle myChartTitel = new TextTitle("",
				new Font("SansSerif", Font.BOLD, FONT_SIZE));
		chart.setTitle(myChartTitel);

		return chart;
	}

	@Override
	public void setDataBinding(DataBindingContext dbCtx, EditingDomain editingDomain, EObject model) {
		super.setDataBinding(dbCtx, editingDomain, model);
		xyPlotChartViewer.setInput(model);
	}

	/**
	 * 
	 * @author yoge_re
	 *
	 */
	private class AvailabilityChartLabelProvider extends VirSatTransactionalAdapterFactoryLabelProvider
			implements ISeriesXYValueLabelProvider {

		// Hard limit the maximum number of entries to be shown to avoid performance issues
		private static final int MAX_POINTS = 100;

		/**
		 * default constructor
		 * 
		 * @param adapterFactory
		 *            the adapter Factory
		 */
		AvailabilityChartLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public String getSeries(Object object) {
			AvailabilityAnalysis availAnalysis = (AvailabilityAnalysis) object;
			Fault fault = availAnalysis.getParentCaBeanOfClass(Fault.class);
			return "Availability of " + fault.getName();
		}

		@Override
		public Double[] getValuesY(Object object) {
			AvailabilityAnalysis availabilityAnalysis = (AvailabilityAnalysis) object;
			IBeanList<BeanPropertyFloat> availabilityCurve = availabilityAnalysis.getAvailabilityCurve();
			
			// Limit the maximum number of visualized points
			int steps = Math.min(MAX_POINTS, availabilityCurve.size());
			double increment = (double) availabilityCurve.size() / steps;
			Double[] availabilityValues = new Double[steps];
			for (int step = 0; step < steps; ++step) {
				int timeStep = (int) (step * increment);
				availabilityValues[step] = availabilityCurve.get(timeStep).getValue();
			}
			
			return availabilityValues;
		}

		@Override
		public Double[] getValuesX(Object object) {
			AvailabilityAnalysis availabilityAnalysis = (AvailabilityAnalysis) object;
			double maxTime = availabilityAnalysis.getRemainingMissionTime();
			IBeanList<BeanPropertyFloat> availabilityCurve = availabilityAnalysis.getAvailabilityCurve();
			
			// Limit the maximum number of visualized points
			int steps = Math.min(MAX_POINTS, availabilityCurve.size());
			double increment = (double) availabilityCurve.size() / steps;
			double delta = maxTime / steps;
			Double[] timeSteps = new Double[steps];
			for (int step = 0; step < steps; ++step) {
				int timeStep = (int) (step * increment);
				timeSteps[step] = timeStep * delta;
			}

			XYPlot plot = chart.getXYPlot();
			plot.getDomainAxis().setUpperBound(maxTime);

			return timeSteps;
		}

	}
}
