package org.brejo.jira.service.services

import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.io.entity.EntityUtils
import java.io.IOException

class JiraJqlService(domain: String) {
    private val uriApi: String

    init {
        uriApi = "https://$domain/jira/rest/api/2/search?jql="
    }

    fun requestJql(jql: String, maxResults: Int): String {
        try {
            HttpClientBuilder.create().build().use { httpClient ->
                val request = "$uriApi$jql&maxResults=$maxResults"
                val httpGet = HttpGet(request)
                return httpClient.execute(
                    httpGet
                ) { response: ClassicHttpResponse ->
                    EntityUtils.toString(
                        response.entity
                    )
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}