package org.brejo.jira.service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Reporter {
    var name: String? = null

    constructor(name: String?) {
        this.name = name
    }

    constructor()
}