package zeal.challenge.objects

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// Fields, irrelevant to this test assignment, were omitted on purpose
@JsonIgnoreProperties(ignoreUnknown = true)
data class PageDetailsObject(
    val id: Int,
    val key: String,
    val title: String,
    val latest: LatestObject
)

data class LatestObject(
    val id: Int,
    val timestamp: String
)
