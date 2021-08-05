package jp.co.betacomputing.simpleqiitaclient

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import jp.co.betacomputing.simpleqiitaclient.data.Data
import jp.co.betacomputing.simpleqiitaclient.ui.Ui
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import jp.co.betacomputing.simpleqiitaclient.application.Application as ApplicationModule

internal class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@MyApp)
            modules(
                module {
                    single(named("DEBUG")) { BuildConfig.DEBUG }
                    single(named("VERSION_CODE")) { BuildConfig.VERSION_CODE }
                    single(named("VERSION_NAME")) { BuildConfig.VERSION_NAME }
                },
                Ui.koinModule,
                ApplicationModule.koinModule,
                Data.koinModule,
            )
        }
    }
}
