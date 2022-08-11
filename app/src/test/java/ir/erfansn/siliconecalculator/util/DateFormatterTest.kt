/*
 * Copyright 2022 Erfan Sn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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