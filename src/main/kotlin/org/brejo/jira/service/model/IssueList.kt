package org.brejo.jira.service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class IssueList {
    var issues: List<Issue>? = null
    var total: Int? = null

    constructor(issues: List<Issue>?, total: Int?) {
        this.issues = issues
        this.total = total
    }

    constructor()
}