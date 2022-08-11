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

package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistory
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override val historyItemsStream = historyDao.getHistoryEntitiesStream()
        .map {
            it.map(HistoryEntity::asHistory)
        }

    override suspend fun clearAllHistory() {
        historyDao.deleteAllHistoryEntities()
    }

    override suspend fun saveCalculation(calculation: Calculation) {
        historyDao.insertHistoryEntity(calculation.asHistoryEntity())
    }
}