dependencies {
    implementation(project(":application"))

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    implementation(Dependencies.Koin.coreExt)
    implementation(Dependencies.Moshi.adapters)
    implementation(Dependencies.Moshi.kotlin)
    implementation(Dependencies.Retrofit.converterMoshi)
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.threeTenAbp)
    implementation(Dependencies.timber)
}
