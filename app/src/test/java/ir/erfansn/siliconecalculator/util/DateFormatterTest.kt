package ir.erfansn.siliconecalculator.util

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.*
import org.junit.Test

class DateFormatterTest {

    @Test
    fun `Returns 'Today' when given Instant is now`() {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val formattedDate = today.format()

        assertThat(formattedDate).isEqualTo("Today")
    }

    @Test
    fun `Returns 'Yesterday' when given Instant was yesterday`() {
        val yesterday =
            Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(1, DateTimeUnit.DAY)

        val formattedDate = yesterday.format()

        assertThat(formattedDate).isEqualTo("Yesterday")
    }

    @Test
    fun `Returns corresponding date when given Instant was a few days ago`() {
        val fewDayAgo =
            Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(2, DateTimeUnit.DAY)

        val formattedDate = fewDayAgo.format()

        assertThat(formattedDate).isNotEqualTo("Today")
        assertThat(formattedDate).isNotEqualTo("Yesterday")
        assertThat(formattedDate).matches("""^\w+ \d+$""".toRegex().pattern)
    }
}