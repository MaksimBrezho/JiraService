package org.brejo.jira.service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Issue {
    var key: String? = null
    var fields: Fields? = null

    constructor(key: String?, fields: Fields?) {
        this.key = key
        this.fields = fields
    }

    constructor()

    @JsonIgnoreProperties(ignoreUnknown = true)
    inner class Fields {
        var reporter: Reporter? = null
        var created: Date? = null
        var resolutiondate: Date? = null

        constructor(reporter: Reporter?, created: Date?, resolutiondate: Date?) {
            this.reporter = reporter
            this.created = created
            this.resolutiondate = resolutiondate
        }

        constructor()
    }
}