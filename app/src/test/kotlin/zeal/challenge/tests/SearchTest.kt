package zeal.challenge.tests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.hasItem
import org.junit.BeforeClass
import zeal.challenge.objects.PageDetailsObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertTrue

const val BASE_URL = "https://api.wikimedia.org/core/v1/wikipedia/en"

class AppTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            // Enable logging in case validation fails
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

    @Test
    fun searchForPageWithTitleMatchingQuery() {
        val searchQuery = "furry rabbits"
        val titleToFind = "Sesame Street"

        Given {
            param("q", searchQuery)
        } When {
            get("$BASE_URL/search/page")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("pages.title", hasItem(titleToFind))
        }
    }

    @Test
    fun checkLatestTimestampForPage() {
        val pageTitle = "Sesame_Street"
        val oldestAcceptableDate = LocalDate.parse("2023-08-17", DateTimeFormatter.ISO_LOCAL_DATE)

        val responseBody =
            When {
                get("$BASE_URL/page/$pageTitle/bare")
            } Then {
                statusCode(HttpStatus.SC_OK)
            } Extract {
                body().asString()
            }

        // Mapping response Json to PageDetails object
        val pageDetailsResponse: PageDetailsObject = jacksonObjectMapper().readValue(responseBody)
        val actualDate =
            LocalDateTime.parse(pageDetailsResponse.latest.timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                .toLocalDate()

        assertTrue(
            actualDate.isAfter(oldestAcceptableDate),
            "Actual date $actualDate is before the oldest acceptable date $oldestAcceptableDate"
        )
    }
}
