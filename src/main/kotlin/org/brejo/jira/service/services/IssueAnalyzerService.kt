package org.brejo.jira.service.services

import com.fasterxml.jackson.core.JsonProcessingException
import org.brejo.jira.service.charts.HistogramCreatedAndClosedIssue
import org.brejo.jira.service.charts.HistogramForCategoryDataset
import org.brejo.jira.service.charts.HistogramForIntervalXYDataset
import org.brejo.jira.service.charts.abstractclass.Histogram
import org.brejo.jira.service.model.IssueList
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class IssueAnalyzerService(domain: String?) {
    private val issueService: IssueService

    init {
        issueService = IssueService(domain)
    }

    @Throws(IOException::class)
    fun drawHistogramTimeSpent(
        chartTitle: String?, project: String?,
        status: String?, maxResults: Int?
    ) {
        val issueList = issueService.getIssuesForProject(project!!, status!!, maxResults)
        val timeAndIssues = transformData(issueList)
        val hist = HistogramForIntervalXYDataset(chartTitle!!, timeAndIssues)
        hist.draw()
    }

    @Throws(IOException::class)
    fun drawHistogramIssueReporter(
        chartTitle: String?, reporter: String?, project: String?,
        status: String?, maxResults: Int?
    ) {
        val issueList = issueService.getIssuesForProjectAndReporter(
            reporter!!,
            project!!, status!!, maxResults
        )
        val topUsers = transformData(issueList)
        val hist = HistogramForIntervalXYDataset(chartTitle!!, topUsers)
        hist.draw()
    }

    @Throws(JsonProcessingException::class)
    fun drawHistogramTopUsers(
        quantityUsers: Int, chartTitle: String?, project: String?,
        status: String?, maxResults: Int?
    ) {
        val issueList = issueService.getIssuesForProject(project!!, status!!, maxResults)
        val topUsers = transformDataTopUsers(issueList, quantityUsers)
        val hist = HistogramForCategoryDataset(chartTitle!!, topUsers.toMutableMap())
        hist.draw()
    }

    @Throws(JsonProcessingException::class)
    fun drawHistogramPriority(
        chartTitle: String?, project: String?,
        priorities: List<String>, maxResults: Int?
    ) {
        val issuePriority: MutableMap<String, Int?> = TreeMap()
        for (priority in priorities) {
            val issueList = issueService.getIssuesForProjectAndPriority(priority, project!!, maxResults)
            issuePriority[priority] = issueList.total
        }
        val hist = HistogramForCategoryDataset(chartTitle!!, issuePriority.toMutableMap())
        hist.draw()
    }

    @Throws(JsonProcessingException::class)
    fun drawHistogramForDaysIssue(
        startDate: String?, endDate: String?, chartTitle: String?, project: String?,
        maxResult: Int?
    ) {
        val issueListCreated = issueService.getCreatedIssue(
            startDate!!, endDate!!,
            project!!, maxResult
        )
        val issueListClosed = issueService.getClosedIssue(startDate, endDate, project, maxResult)
        var date: String
        val issueCreated: MutableMap<String, Int> = TreeMap()
        for (issue in issueListCreated.issues!!) {
            date = dateToLocalDate(issue.fields!!.created).toString()
            if (issueCreated.containsKey(date)) {
                issueCreated[date] = issueCreated[date]!! + 1
            } else {
                issueCreated[date] = 1
            }
        }
        val issueClosed: MutableMap<String, Int> = TreeMap()
        for (issue in issueListClosed.issues!!) {
            date = dateToLocalDate(issue.fields!!.resolutiondate).toString()
            if (issueClosed.containsKey(date)) {
                issueClosed[date] = issueCreated[date]!! + 1
            } else {
                issueClosed[date] = 1
            }
        }
        val issueAccumulated: MutableMap<String, Int> = TreeMap()
        var accum = 0
        for (key in issueCreated.keys) {
            val quantityCreated = issueCreated[key]!!
            accum = if (issueClosed.containsKey(key)) {
                accum + quantityCreated - issueClosed[key]!!
            } else {
                accum + quantityCreated
            }
            if (accum < 0) {
                accum = 0
            }
            issueAccumulated[key] = accum
        }
        val hist: Histogram = HistogramCreatedAndClosedIssue(chartTitle!!, issueCreated, issueClosed, issueAccumulated)
        hist.draw()
    }


    private fun dateToLocalDate(date: Date?): LocalDate {
        return date!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun transformDataTopUsers(issueList: IssueList, quantityUsers: Int): Map<String?, Int?> {
        val result: MutableMap<String?, Int?> = TreeMap()
        for (issue in issueList.issues!!) {
            if (result.size > quantityUsers) break
            val reporterName = issue.fields!!.reporter!!.name
            if (result.containsKey(reporterName)) {
                result[reporterName] = result[reporterName]!! + 1
            } else {
                result[reporterName] = 1
            }
        }
        return result
    }

    private fun transformData(issueList: IssueList): Map<Int, Int> {
        val MILLI_TO_HOUR = (1000 * 60 * 60).toLong()
        val result: MutableMap<Int, Int> = TreeMap()
        for (issue in issueList.issues!!) {
            val created = issue.fields!!.created
            val resolutiondate = issue.fields!!.resolutiondate
            val diffDateInHour = ((resolutiondate!!.time - created!!.time) / MILLI_TO_HOUR).toInt()
            if (diffDateInHour < 0) throw RuntimeException()
            if (result.containsKey(diffDateInHour)) {
                result[diffDateInHour] = result[diffDateInHour]!! + 1
            } else {
                result[diffDateInHour] = 1
            }
        }
        return result
    }
}