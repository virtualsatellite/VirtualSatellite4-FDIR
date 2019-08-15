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
import de.dlr.sc.virsat.model.dvlm.qudv.util.QudvUnitHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.AvailabilityAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.DetectabilityAnalysis;
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
public class UiSnippetTableDetectabilityAnalysisDetectabilityCurve extends AUiSnippetTableDetectabilityAnalysisDetectabilityCurve implements IUiSnippet {
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

		xyPlotChartViewer.setLabelProvider(new DetectabilityChartLabelProvider(adapterFactory));
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
		DetectabilityAnalysis detectabilityAnalysis = new DetectabilityAnalysis((CategoryAssignment) model);
		String missionTimeUnit = detectabilityAnalysis.getRemainingMissionTimeBean().getTypeInstance().getUnit()
				.getSymbol();

		dataset = new XYSeriesCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(null, "Mission Time [" + missionTimeUnit + "]",
				"Detectability [%]", dataset);

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
		renderer.setBaseShapesVisible(false);

		// set the title
		TextTitle myChartTitel = new TextTitle("Detectability [%]",
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
	private class DetectabilityChartLabelProvider extends VirSatTransactionalAdapterFactoryLabelProvider
			implements ISeriesXYValueLabelProvider {

		/**
		 * default constructor
		 * 
		 * @param adapterFactory
		 *            the adapter Factory
		 */
		DetectabilityChartLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public String getSeries(Object object) {
			DetectabilityAnalysis detectAnalysis = (DetectabilityAnalysis) object;
			Fault fault = detectAnalysis.getParentCaBeanOfClass(Fault.class);
			if (fault != null) {
				return "Detectability of " + fault.getName();
			} else {
				return "Please add a fault";
			}
		}

		@Override
		public Double[] getValuesX(Object object) {
			DetectabilityAnalysis detectAnalysis = (DetectabilityAnalysis) object;
			double timestep = detectAnalysis.getTimestepBean().getValueToBaseUnit();
			double maxTime = detectAnalysis.getRemainingMissionTime();
			double delta = QudvUnitHelper.getInstance().convertFromBaseUnitToTargetUnit(
					detectAnalysis.getRemainingMissionTimeBean().getTypeInstance().getUnit(), timestep);
			int steps = (int) (maxTime / delta);
			Double[] timeSteps = new Double[steps + 1];
			for (int time = 0; time <= steps; ++time) {
				timeSteps[time] = time * delta;
			}

			XYPlot plot = chart.getXYPlot();
			plot.getDomainAxis().setUpperBound(maxTime);

			return timeSteps;
		}

		@Override
		public Double[] getValuesY(Object object) {
			DetectabilityAnalysis detectAnalysis = (DetectabilityAnalysis) object;
			IBeanList<BeanPropertyFloat> detectCurve = detectAnalysis.getDetectabilityCurve();
			Double[] detectValues = detectCurve.stream().mapToDouble(beanFloat -> beanFloat.getValue())
					.boxed().toArray(Double[]::new);
			return detectValues;
		}

	}
}
