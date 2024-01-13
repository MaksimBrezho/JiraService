package org.brejo.jira.service.charts.abstractclass

import javax.swing.JFrame
import javax.swing.SwingUtilities

abstract class Histogram(title: String?) : JFrame(title) {
    fun draw() {
        SwingUtilities.invokeLater {
            super.setSize(800, 600)
            super.setLocationRelativeTo(null)
            super.setDefaultCloseOperation(EXIT_ON_CLOSE)
            super.setVisible(true)
        }
    }
}