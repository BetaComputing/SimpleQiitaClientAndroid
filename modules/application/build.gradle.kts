dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.threeTenAbp)
    implementation(Dependencies.timber)

    compileOnly(Dependencies.AndroidX.Compose.UI.tooling)
}
