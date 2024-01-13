package org.brejo.jira.service.charts

import org.brejo.jira.service.charts.abstractclass.Histogram
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.DefaultCategoryDataset
import java.awt.Color
import java.awt.Dimension

class HistogramForCategoryDataset(title: String, data: MutableMap<String?, Int?>) : Histogram(title) {
    init {

        // Создаем датасет с данными
        val dataset = createDataset(data)

        // Создаем гистограмму
        val chart = createChart(dataset, title)

        // Создаем панель для графика
        val chartPanel = ChartPanel(chart)
        chartPanel.preferredSize = Dimension(800, 400)
        contentPane = chartPanel
    }

    private fun createDataset(data: Map<String?, Int?>): DefaultCategoryDataset {
        val dataset = DefaultCategoryDataset()
        for ((key, value) in data) {
            dataset.addValue(value, key, key)
        }
        return dataset
    }

    private fun createChart(dataset: DefaultCategoryDataset, title: String): JFreeChart {
        val chart = ChartFactory.createBarChart(
            title,
            "Users",
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
        domainAxis.axisLinePaint = Color.black
        domainAxis.tickMarkPaint = Color.black
        domainAxis.tickLabelPaint = Color.black
        val rangeAxis = plot.rangeAxis
        rangeAxis.axisLinePaint = Color.black
        rangeAxis.tickMarkPaint = Color.black
        rangeAxis.tickLabelPaint = Color.black
        return chart
    }
}