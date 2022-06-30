package ir.erfansn.siliconecalculator.di

import android.content.Context
import androidx.room.Room
import ir.erfansn.siliconecalculator.data.repository.HistoryRepositoryImpl
import ir.erfansn.siliconecalculator.data.source.local.HistoryLocalDataSource
import ir.erfansn.siliconecalculator.data.source.local.db.SiliconeCalculatorDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppContainer(context: Context) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val database =
        Room.databaseBuilder(
            context,
            SiliconeCalculatorDatabase::class.java,
            "silicone_calculator"
        ).build()

    private val historyDao = database.historyDao()

    private val historyLocalDataSource = HistoryLocalDataSource(historyDao, ioDispatcher)

    val historyRepository = HistoryRepositoryImpl(historyLocalDataSource)
}