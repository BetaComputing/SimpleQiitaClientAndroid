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
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.Lifecycle.liveDataKtx)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.material)
    implementation(Dependencies.timber)

    compileOnly(Dependencies.AndroidX.Compose.UI.tooling)
}
