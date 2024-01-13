import com.fasterxml.jackson.core.JsonProcessingException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.brejo.jira.service.model.Issue
import org.brejo.jira.service.model.IssueList
import org.brejo.jira.service.model.Reporter
import org.brejo.jira.service.services.IssueService

class IssueServiceTest {

    private val issueService = IssueService("issues.apache.org")

    @Test
    @Throws(JsonProcessingException::class)
    fun getIssuesForProjectReturnTest() {
        val issueList: IssueList = issueService.getIssuesForProject("KAFKA", "Closed", 1000)
        checkClosedIssueList(issueList)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getIssuesForProjectAndReporterReturnTest() {
        val issueList: IssueList = issueService.getIssuesForProjectAndReporter("lihaosky", "KAFKA", "Closed", 1000)
        checkReporterIssueList(issueList)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getIssuesForProjectAndPriorityReturnTest() {
        val issueList: IssueList = issueService.getIssuesForProjectAndPriority("Blocker", "KAFKA", 1000)
        checkIssueList(issueList)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getCreatedIssueReturnTest() {
        val issueList: IssueList = issueService.getCreatedIssue("2023-11-01", "2023-11-30", "KAFKA", 1000)
        checkIssueList(issueList)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getClosedIssueReturnTest() {
        val issueList: IssueList = issueService.getCreatedIssue("2023-11-01", "2023-11-30", "KAFKA", 1000)
        checkIssueList(issueList)
    }

    private fun checkReporterIssueList(issueList: IssueList) {
        checkClosedIssueList(issueList)
        for (issue in issueList.issues!!) {
            val fields: Issue.Fields? = issue.fields
            val reporter: Reporter? = fields?.reporter
            assertNotNull(reporter)
            if (reporter != null) {
                assertNotNull(reporter.name)
            }
        }
    }

    private fun checkClosedIssueList(issueList: IssueList) {
        checkIssueList(issueList)
        for (issue in issueList.issues!!) {
            val fields: Issue.Fields? = issue.fields
            if (fields != null) {
                assertNotNull(fields.resolutiondate)
            }
        }
    }

    private fun checkIssueList(issueList: IssueList) {
        assertNotNull(issueList)
        assertNotNull(issueList.total)
        assertNotNull(issueList.issues)
        for (issue in issueList.issues!!) {
            assertNotNull(issue)
            assertNotNull(issue.key)
            val fields: Issue.Fields? = issue.fields
            assertNotNull(fields)
            if (fields != null) {
                assertNotNull(fields.created)
            }
        }
    }
}
