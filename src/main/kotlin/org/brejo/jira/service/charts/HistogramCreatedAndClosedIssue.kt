package org.brejo.jira.service.charts

import org.brejo.jira.service.charts.abstractclass.Histogram
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.DefaultCategoryDataset
import java.awt.Color
import java.awt.Dimension

class HistogramCreatedAndClosedIssue(
    title: String, dataCreated: Map<String, Int>,
    dataClosed: Map<String, Int>, dataAccumulated: Map<String, Int>
) : Histogram(title) {
    init {

        // Создаем датасет с данными
        val dataset = createDataset(dataCreated, dataClosed, dataAccumulated)

        // Создаем гистограмму
        val chart = createChart(dataset, title)

        // Создаем панель для графика
        val chartPanel = ChartPanel(chart)
        chartPanel.preferredSize = Dimension(800, 400)
        contentPane = chartPanel
    }

    private fun createDataset(
        dataCreated: Map<String, Int>, dataClosed: Map<String, Int>,
        dataAccumulated: Map<String, Int>
    ): DefaultCategoryDataset {
        val dataset = DefaultCategoryDataset()
        for ((key, value) in dataCreated) {
            dataset.addValue(value, "Created", key)
        }
        for ((key, value) in dataClosed) {
            dataset.addValue(value, "Closed", key)
        }
        for ((key, value) in dataAccumulated) {
            dataset.addValue(value, "Accumulated", key)
        }
        return dataset
    }

    private fun createChart(dataset: DefaultCategoryDataset, title: String): JFreeChart {
        val chart = ChartFactory.createBarChart(
            title,
            "Time",
            "Issue",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            true
        )
        val plot = chart.categoryPlot
        plot.backgroundPaint = Color.white
        val domainAxis = plot.domainAxis
        domainAxis.axisLinePaint = Color.darkGray
        domainAxis.tickMarkPaint = Color.darkGray
        domainAxis.tickLabelPaint = Color.darkGray
        val rangeAxis = plot.rangeAxis
        rangeAxis.axisLinePaint = Color.darkGray
        rangeAxis.tickMarkPaint = Color.darkGray
        rangeAxis.tickLabelPaint = Color.darkGray
        return chart
    }
}
