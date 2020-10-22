dependencies {
    implementation(project(":common"))
    implementation(project(":ui"))
    implementation(project(":application"))
    implementation(project(":data"))

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.Koin.androidXExt)
    implementation(Dependencies.threeTenAbp)
    implementation(Dependencies.timber)

    debugImplementation(Dependencies.Hyperion.core)
    debugImplementation(Dependencies.Hyperion.crash)
    debugImplementation(Dependencies.Hyperion.sharedPreferences)
    debugImplementation(Dependencies.Hyperion.timber)
    debugImplementation(Dependencies.leakCanary)
}
