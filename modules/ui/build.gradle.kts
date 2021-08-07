plugins { kotlin("kapt") }

dependencies {
    implementation(project(":common"))
    implementation(project(":application"))

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.activityCompose)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.Compose.UI.ui)
    implementation(Dependencies.AndroidX.Compose.UI.tooling)
    implementation(Dependencies.AndroidX.Compose.Foundation.foundation)
    implementation(Dependencies.AndroidX.Compose.Material.material)
    implementation(Dependencies.AndroidX.Compose.Material.iconsCore)
    implementation(Dependencies.AndroidX.Compose.Runtime.runtime)
    implementation(Dependencies.AndroidX.Compose.Runtime.liveData)
    implementation(Dependencies.AndroidX.Lifecycle.coreKtx)
    implementation(Dependencies.AndroidX.Lifecycle.liveDataKtx)
    implementation(Dependencies.AndroidX.Lifecycle.viewModelKtx)
    implementation(Dependencies.Accompanist.flowLayout)
    implementation(Dependencies.Coil.compose)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.Koin.androidXExt)
    implementation(Dependencies.Koin.viewModel)
    implementation(Dependencies.material)
    implementation(Dependencies.threeTenAbp)
    implementation(Dependencies.timber)

    testImplementation(Dependencies.Kotlin.Coroutines.test)
    testImplementation(Dependencies.AndroidX.Test.coreKtx)
    testImplementation(Dependencies.Robolectric.robolectric)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockK)
    testImplementation(Dependencies.threeTenBp) {
        exclude(Dependencies.threeTenAbp)
    }
}
