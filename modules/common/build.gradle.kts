plugins { kotlin("kapt") }

android {
    lintOptions {
        xmlReport = true
        isAbortOnError = true
        setDisable(setOf("ObsoleteLintCustomCheck", "MediaCapabilities", "UnusedResources", "NullSafeMutableLiveData"))
    }
}

dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.Lifecycle.coreKtx)
    implementation(Dependencies.AndroidX.Lifecycle.liveDataKtx)
    implementation(Dependencies.AndroidX.Lifecycle.viewModelKtx)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.Koin.androidXExt)
    implementation(Dependencies.Koin.viewModel)
    implementation(Dependencies.glide)
    implementation(Dependencies.material)
    implementation(Dependencies.timber)
}
