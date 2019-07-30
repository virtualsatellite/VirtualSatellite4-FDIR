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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import de.dlr.sc.virsat.commons.ui.jface.viewer.ISeriesXYValueLabelProvider;
import de.dlr.sc.virsat.commons.ui.jface.viewer.XYSplineChartViewer;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityAnalysis;
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
public class UiSnippetTableReliabilityAnalysisReliabilityCurve extends AUiSnippetTableReliabilityAnalysisReliabilityCurve implements IUiSnippet {
	private static final int DEFAULT_CHART_HEIGHT = 300;
	private static final int FONT_SIZE = 12;
	private JFreeChart chart;
	private ChartComposite chartComposite;
	private XYSplineChartViewer xyPlotChartViewer;

	private XYSeriesCollection dataset;
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {

		Composite subSection = toolkit.createComposite(composite);
		GridLayout layout = new GridLayout(2, true);
		subSection.setLayout(layout);
		super.createSwt(toolkit, editingDomain, subSection, initModel);
		GridData gridDataReliabilityChart = createDefaultGridData();
		gridDataReliabilityChart.horizontalSpan = 1;
		gridDataReliabilityChart.minimumHeight = DEFAULT_CHART_HEIGHT;
		gridDataReliabilityChart.heightHint = DEFAULT_CHART_HEIGHT;
		subSection.setLayoutData(gridDataReliabilityChart);


		chart = createChart();
		chart.setBorderVisible(false);

		chartComposite = new ChartComposite(subSection, SWT.NONE, chart, true);
		chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		xyPlotChartViewer = new XYSplineChartViewer(dataset, chartComposite);
		xyPlotChartViewer.setContentProvider(new VirSatFilteredWrappedTreeContentProvider(
				new VirSatTransactionalAdapterFactoryContentProvider(adapterFactory).setUpdateCaContainerLabels(true)) {
			@Override
			public Object[] getElements(Object inputElement) {
				ReliabilityAnalysis relAnalysis = new ReliabilityAnalysis((CategoryAssignment) inputElement);
				return new Object[] { relAnalysis };
			}
		});

		xyPlotChartViewer.setLabelProvider(new ReliabilityChartLabelProvider(adapterFactory));
		xyPlotChartViewer.setInput(model);
		
	


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
		ReliabilityAnalysis reliabilityAnalysis = new ReliabilityAnalysis((CategoryAssignment) model);
		String missionTimeUnit = reliabilityAnalysis.getRemainingMissionTimeBean().getTypeInstance().getUnit()
				.getSymbol();

		dataset = new XYSeriesCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(null, "Mission Time [" + missionTimeUnit + "]",
				"Reliability [%]", dataset);

		XYPlot plot = chart.getXYPlot();
		plot.setNoDataMessage("No data available");
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.GRAY);
		chart.setAntiAlias(true);
		chart.setBorderVisible(true);
		chart.setBackgroundPaint(Color.white);
		plot.getRangeAxis().setRange(0, TO_PERCENT);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
		renderer.setBaseShapesVisible(false);

		// set the title
		TextTitle myChartTitel = new TextTitle("Reliability / Mission Success [%]",
				new Font("SansSerif", Font.BOLD, FONT_SIZE));
		chart.setTitle(myChartTitel);

		return chart;
	}
	/**
	 * Provides the reliabilty data to the chart
	 * 
	 * @author muel_s8
	 *
	 */
	private class ReliabilityChartLabelProvider extends VirSatTransactionalAdapterFactoryLabelProvider
			implements ISeriesXYValueLabelProvider {

		/**
		 * Default constructor
		 * 
		 * @param adapterFactory
		 *            the adapter factory
		 */
		ReliabilityChartLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public Double[] getValuesY(Object object) {
			ReliabilityAnalysis relAnalysis = (ReliabilityAnalysis) object;
			IBeanList<BeanPropertyFloat> reliabilityCurve = relAnalysis.getReliabilityCurve();
			Double[] reliabilityValues = reliabilityCurve.stream().mapToDouble(beanFloat -> beanFloat.getValue())
					.boxed().toArray(Double[]::new);
			return reliabilityValues;
		}

		@Override
		public Double[] getValuesX(Object object) {
			ReliabilityAnalysis relAnalysis = (ReliabilityAnalysis) object;
			double maxTime = relAnalysis.getRemainingMissionTime();
			double delta = maxTime / ReliabilityAnalysis.COUNT_RELIABILITY_POINTS;
			int steps = (int) (maxTime / delta);
			Double[] timeSteps = new Double[steps + 1];
			for (int time = 0; time <= steps; ++time) {
				timeSteps[time] = time * delta;
			}

			XYPlot plot = chart.getXYPlot();

			double mttf = relAnalysis.getMeanTimeToFailure();

			final Marker mttfMarker = new ValueMarker(mttf);
			mttfMarker.setPaint(Color.red);
			mttfMarker.setLabel("MTTF = " + mttf);
			mttfMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
			mttfMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);

			plot.addDomainMarker(mttfMarker, Layer.BACKGROUND);
			plot.getDomainAxis().setUpperBound(maxTime);

			return timeSteps;
		}

		@Override
		public String getSeries(Object object) {
			ReliabilityAnalysis relAnalysis = (ReliabilityAnalysis) object;
			Fault fault = relAnalysis.getFault();
			if (fault != null) {
				return "Reliability of " + fault.getName();
			} else {
				return "Please add a fault";
			}
		}
	}
}
