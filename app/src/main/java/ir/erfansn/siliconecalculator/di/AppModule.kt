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

package ir.erfansn.siliconecalculator.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.data.repository.HistoryRepositoryImpl
import ir.erfansn.siliconecalculator.data.source.local.db.SiliconeCalculatorDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindsHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl,
    ): HistoryRepository

    companion object {

        @[Provides Singleton]
        fun providesRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context,
            SiliconeCalculatorDatabase::class.java,
            "silicone_calculator"
        ).build()

        @Provides
        fun providesHistoryDao(siliconeCalculatorDatabase: SiliconeCalculatorDatabase) =
            siliconeCalculatorDatabase.historyDao()
    }
}