package net.aridai.simpleqiitaclient.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object Ui {
    val koinModule: Module = module {
        viewModel { MainViewModel() }
    }
}
