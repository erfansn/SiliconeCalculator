package ir.erfansn.siliconecalculator

import android.app.Application
import ir.erfansn.siliconecalculator.di.AppContainer

class SiliconeCalculator : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}
