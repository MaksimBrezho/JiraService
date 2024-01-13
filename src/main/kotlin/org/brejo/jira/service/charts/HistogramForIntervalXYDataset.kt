package org.brejo.jira.service.charts

import org.brejo.jira.service.charts.abstractclass.Histogram
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYBarRenderer
import org.jfree.data.xy.IntervalXYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.awt.Dimension

class HistogramForIntervalXYDataset(title: String, data: Map<Int, Int>) : Histogram(title) {
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

    private fun createDataset(data: Map<Int, Int>): IntervalXYDataset {
        val series = XYSeries("Issue")
        for ((key, value) in data) {
            series.add(key, value)
        }
        return XYSeriesCollection(series)
    }

    private fun createChart(dataset: IntervalXYDataset, title: String): JFreeChart {
        val chart = ChartFactory.createXYBarChart(
            title,
            "Time in hours",
            false,
            "Issue",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        )
        val plot = chart.plot as XYPlot
        val renderer = plot.renderer as XYBarRenderer
        renderer.margin = 0.1
        val myColor = Color(255, 0, 128)
        plot.backgroundPaint = Color.white
        plot.domainGridlinePaint = Color.lightGray
        plot.rangeGridlinePaint = Color.lightGray
        renderer.setSeriesPaint(0, Color.blue)
        chart.backgroundPaint = Color.white
        plot.isDomainPannable = true
        plot.isRangePannable = true
        plot.domainAxis.lowerBound = 0.0
        return chart
    }
}