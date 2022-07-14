package ir.erfansn.siliconecalculator.util

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.time.Duration.Companion.days

class InstantFormatterTest {

    @Test
    fun `Returns 'Today' when given Instant is now`() {
        val today = Clock.System.now()

        val formattedDate = today.formatToString()

        assertThat(formattedDate).isEqualTo("Today")
    }

    @Test
    fun `Returns 'Yesterday' when given Instant was yesterday`() {
        val yesterday = Clock.System.now() - 1.days

        val formattedDate = yesterday.formatToString()

        assertThat(formattedDate).isEqualTo("Yesterday")
    }

    @Test
    fun `Returns corresponding date when given Instant was a few days ago`() {
        val fewDayAgo = Clock.System.now() - 2.days

        val formattedDate = fewDayAgo.formatToString()

        assertThat(formattedDate).isNotEqualTo("Today")
        assertThat(formattedDate).isNotEqualTo("Yesterday")
        assertThat(formattedDate).matches("""^\w+ \d+$""".toRegex().pattern)
    }
}