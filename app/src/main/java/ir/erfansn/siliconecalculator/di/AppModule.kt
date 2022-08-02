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