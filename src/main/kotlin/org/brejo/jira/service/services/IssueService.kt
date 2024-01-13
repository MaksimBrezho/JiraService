package org.brejo.jira.service.services

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.brejo.jira.service.model.IssueList

class IssueService(domain: String?) {
    private val objectMapper: ObjectMapper
    private val jiraJqlService: JiraJqlService

    init {
        objectMapper = ObjectMapper()
        jiraJqlService = domain?.let { JiraJqlService(it) }!!
    }

    @Throws(JsonProcessingException::class)
    fun getIssuesForProject(project: String, status: String, maxResults: Int?): IssueList {
        val jql = PROJECT + project + AND + STATUS + status
        val response = maxResults?.let { jiraJqlService.requestJql(jql, it) }
        return objectMapper.readValue(response, IssueList::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun getIssuesForProjectAndReporter(reporter: String, project: String, status: String, maxResults: Int?): IssueList {
        val jql = REPORTER + reporter + AND + PROJECT + project + AND + STATUS + status
        val response = maxResults?.let { jiraJqlService.requestJql(jql, it) }
        return objectMapper.readValue(response, IssueList::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun getIssuesForProjectAndPriority(priority: String, project: String, maxResults: Int?): IssueList {
        val jql = PRIORITY + priority + AND + PROJECT + project
        val response = maxResults?.let { jiraJqlService.requestJql(jql, it) }
        return objectMapper.readValue(response, IssueList::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun getCreatedIssue(startDate: String, endDate: String, project: String, maxResults: Int?): IssueList {
        val jql = PROJECT + project + AND + CREATED_HIGH + startDate + AND + CREATED_LOW + endDate
        val response = maxResults?.let { jiraJqlService.requestJql(jql, it) }
        return objectMapper.readValue(response, IssueList::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun getClosedIssue(startDate: String, endDate: String, project: String, maxResults: Int?): IssueList {
        val jql = PROJECT + project + AND + CLOSED_HIGH + startDate + AND + CLOSED_LOW + endDate
        val response = maxResults?.let { jiraJqlService.requestJql(jql, it) }
        return objectMapper.readValue(response, IssueList::class.java)
    }

    companion object {
        private const val PROJECT = "project="
        private const val STATUS = "status="
        private const val AND = "%20AND%20"
        private const val REPORTER = "reporter="
        private const val PRIORITY = "priority="
        private const val CREATED_HIGH = "created%3E="
        private const val CREATED_LOW = "created%3C="
        private const val CLOSED_HIGH = "resolutiondate%3E="
        private const val CLOSED_LOW = "resolutiondate%3C="
    }
}