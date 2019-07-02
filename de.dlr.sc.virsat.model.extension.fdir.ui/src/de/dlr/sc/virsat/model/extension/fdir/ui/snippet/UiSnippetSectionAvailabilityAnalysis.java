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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
public class UiSnippetSectionAvailabilityAnalysis extends AUiSnippetSectionAvailabilityAnalysis implements IUiSnippet {
	private static final int DEFAULT_CHART_HEIGHT = 600;
	private static final int FONT_SIZE = 12;

	private JFreeChart chart;
	private ChartComposite chartComposite;
	private Composite subSectionBody;

	private XYSeriesCollection dataset;

	private XYSplineChartViewer xyPlotChartViewer;

	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);

		Composite buttonSectionBody = toolkit.createComposite(composite);
		GridLayout layout = new GridLayout(1, true);
		buttonSectionBody.setLayout(layout);
		Button buttonUpdate = toolkit.createButton(buttonSectionBody, "Perform Analysis", SWT.PUSH);
		buttonUpdate.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				Job job = new Job("Availability Analysis") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.setTaskName("Availability Analysis");
						AvailabilityAnalysis availAnalysis = new AvailabilityAnalysis((CategoryAssignment) model);
						Command availabilityAnalysisCommand = availAnalysis
								.perform((TransactionalEditingDomain) editingDomain, monitor);
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
						editingDomain.getCommandStack().execute(availabilityAnalysisCommand);
						monitor.done();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
				xyPlotChartViewer.refresh();
			}
		});

		subSectionBody = toolkit.createComposite(composite);
		layout = new GridLayout(1, true);
		subSectionBody.setLayout(layout);
		GridData gridDataAvailabilityChart = createDefaultGridData();
		gridDataAvailabilityChart.horizontalSpan = 1;
		gridDataAvailabilityChart.minimumHeight = DEFAULT_CHART_HEIGHT;
		gridDataAvailabilityChart.heightHint = DEFAULT_CHART_HEIGHT;
		subSectionBody.setLayoutData(gridDataAvailabilityChart);

		// make a chart
		chart = createChart();
		chart.setBorderVisible(false);

		chartComposite = new ChartComposite(subSectionBody, SWT.NONE, chart, true);
		chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		xyPlotChartViewer = new XYSplineChartViewer(dataset, chartComposite);
		xyPlotChartViewer.setContentProvider(new VirSatFilteredWrappedTreeContentProvider(
				new VirSatTransactionalAdapterFactoryContentProvider(adapterFactory).setUpdateCaContainerLabels(true)) {
			@Override
			public Object[] getElements(Object inputElement) {
				AvailabilityAnalysis relAnalysis = new AvailabilityAnalysis((CategoryAssignment) inputElement);
				return new Object[] { relAnalysis };
			}
		});

		xyPlotChartViewer.setLabelProvider(new AvailabilityChartLabelProvider(adapterFactory));
		xyPlotChartViewer.setInput(model);
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
		chart.setBorderVisible(true);
		chart.setBackgroundPaint(Color.white);
		plot.getRangeAxis().setRange(0, TO_PERCENT);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
		renderer.setBaseShapesVisible(false);

		// set the title
		TextTitle myChartTitel = new TextTitle("Availability / Mission Success [%]",
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
			Fault fault = availAnalysis.getFault();
			if (fault != null) {
				return "Availability of " + fault.getName();
			} else {
				return "Please add a fault";
			}
		}

		@Override
		public Double[] getValuesX(Object object) {
			AvailabilityAnalysis availAnalysis = (AvailabilityAnalysis) object;
			double maxTime = availAnalysis.getRemainingMissionTime();
			double delta = maxTime / AvailabilityAnalysis.COUNT_AVAILABILITY_POINTS;
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
			AvailabilityAnalysis availAnalysis = (AvailabilityAnalysis) object;
			IBeanList<BeanPropertyFloat> availabilityCurve = availAnalysis.getPointAvailabilityCurve();
			Double[] availabilityValues = availabilityCurve.stream().mapToDouble(beanFloat -> beanFloat.getValue())
					.boxed().toArray(Double[]::new);
			return availabilityValues;
		}

	}
}
