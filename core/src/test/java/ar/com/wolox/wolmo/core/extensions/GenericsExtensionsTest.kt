package ar.com.wolox.wolmo.core.extensions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GenericsExtensionsTest {

    @Test
    fun `given a null string or else a not null string when get length then it returns the not null length`() {
        val nullString: String? = null
        val notNullString = "notnullstring"

        assertThat(nullString.orElse(notNullString).length, `is`(13))
    }

    @Test
    fun `given a nullable but not null string or else a not null string when get length then it returns the first not null length`() {
        val nullString: String? = "nullablestring"
        val notNullString = "string"

        assertThat(nullString.orElse(notNullString).length, `is`(14))
    }
}