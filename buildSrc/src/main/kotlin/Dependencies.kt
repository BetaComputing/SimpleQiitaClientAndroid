object Dependencies {

    //  https://mvnrepository.com/artifact/org.jetbrains.kotlin
    object Kotlin {

        //  https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Project.kotlinVersion}"

        object Coroutines {
            private const val version = "1.3.9"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-jdk8
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$version"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-android
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    //  https://mvnrepository.com/artifact/androidx
    object AndroidX {

        //  https://mvnrepository.com/artifact/androidx.appcompat/appcompat
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"

        //  https://mvnrepository.com/artifact/androidx.constraintlayout/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.2"

        //  https://mvnrepository.com/artifact/androidx.core/core-ktx
        const val coreKtx = "androidx.core:core-ktx:1.3.2"

        //  https://mvnrepository.com/artifact/androidx.fragment/fragment-ktx
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"

        //  https://mvnrepository.com/artifact/androidx.lifecycle
        object Lifecycle {
            private const val version = "2.2.0"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-livedata-core-ktx
            const val coreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-livedata-ktx
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-viewmodel-ktx
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        //  https://mvnrepository.com/artifact/androidx.test
        object Test {
            private const val version = "1.3.0"

            //  https://mvnrepository.com/artifact/androidx.test/core-ktx
            const val coreKtx = "androidx.test:core-ktx:$version"
        }
    }

    //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion
    object Hyperion {
        private const val version = "0.9.30"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-core
        const val core = "com.willowtreeapps.hyperion:hyperion-core:$version"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-crash
        const val crash = "com.willowtreeapps.hyperion:hyperion-crash:$version"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-shared-preferences
        const val sharedPreferences = "com.willowtreeapps.hyperion:hyperion-shared-preferences:0.9.27"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-timber
        const val timber = "com.willowtreeapps.hyperion:hyperion-timber:$version"
    }

    //  https://mvnrepository.com/artifact/org.koin
    object Koin {
        private const val version = "2.1.6"

        //  https://mvnrepository.com/artifact/org.koin/koin-core-ext
        const val coreExt = "org.koin:koin-core-ext:$version"

        //  https://mvnrepository.com/artifact/org.koin/koin-androidx-ext
        const val androidXExt = "org.koin:koin-androidx-ext:$version"

        //  https://mvnrepository.com/artifact/org.koin/koin-androidx-viewmodel
        const val viewModel = "org.koin:koin-androidx-viewmodel:$version"
    }

    //  https://mvnrepository.com/artifact/org.robolectric
    object Robolectric {
        private const val version = "4.4"

        //  https://mvnrepository.com/artifact/org.robolectric/robolectric
        const val robolectric = "org.robolectric:robolectric:$version"
    }

    //  https://mvnrepository.com/artifact/com.squareup.leakcanary/leakcanary-android
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.5"

    //  https://mvnrepository.com/artifact/com.google.android.material/material
    const val material = "com.google.android.material:material:1.2.1"

    //  https://mvnrepository.com/artifact/com.jakewharton.timber/timber
    const val timber = "com.jakewharton.timber:timber:4.7.1"

    //  https://mvnrepository.com/artifact/junit/junit
    const val junit = "junit:junit:4.13"

    //  https://mvnrepository.com/artifact/io.mockk/mockk
    const val mockK = "io.mockk:mockk:1.10.2"
}
