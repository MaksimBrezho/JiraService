package org.brejo.jira.service

import org.brejo.jira.service.services.IssueAnalyzerService
import java.io.IOException

object Main {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var project: String
        val domain = args[1]
        val issueAnalyzerService = IssueAnalyzerService(domain)
        var title: String
        if (args[0] == "time-spent") {
            project = args[2]
            title = args[3]
            issueAnalyzerService.drawHistogramTimeSpent(
                title, project,
                "Closed", 1000
            )
        }
        if (args[0] == "quantity-issue") {
            project = args[2]
            title = args[3]
            val startData = args[4]
            val endData = args[5]
            issueAnalyzerService.drawHistogramForDaysIssue(
                startData, endData,
                title, project, 1000
            )
        }
        if (args[0] == "top-users") {
            project = args[2]
            title = args[3]
            val quantityUsers = args[4]
            issueAnalyzerService.drawHistogramTopUsers(
                quantityUsers.toInt(), title, project,
                "Closed", 1000
            )
        }
        if (args[0] == "user-time-spent") {
            project = args[2]
            title = args[3]
            val user = args[4]
            issueAnalyzerService.drawHistogramIssueReporter(
                title, user, project,
                "Closed", 1000
            )
        }
        if (args[0] == "issue-priorities") {
            project = args[2]
            title = args[3]
            issueAnalyzerService.drawHistogramPriority(
                title,
                project,
                listOf("Blocker", "Critical", "Major", "Minor", "Trivial"),
                1
            )
        }
    }
}