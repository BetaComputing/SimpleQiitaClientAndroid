object Dependencies {

    //  https://mvnrepository.com/artifact/org.jetbrains.kotlin
    object Kotlin {

        //  https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Project.kotlinVersion}"

        object Coroutines {
            private const val version = "1.5.1"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-android
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

            //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    //  https://mvnrepository.com/artifact/androidx
    object AndroidX {

        //  https://mvnrepository.com/artifact/androidx.appcompat/appcompat
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        //  https://mvnrepository.com/artifact/androidx.constraintlayout/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0"

        //  https://mvnrepository.com/artifact/androidx.core/core-ktx
        const val coreKtx = "androidx.core:core-ktx:1.6.0"

        //  https://mvnrepository.com/artifact/androidx.activity/activity-compose
        const val activityCompose = "androidx.activity:activity-compose:1.3.1"

        //  https://mvnrepository.com/artifact/androidx.fragment/fragment-ktx
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.6"

        //  https://mvnrepository.com/artifact/androidx.compose
        object Compose {
            private const val version = Project.composeVersion

            //  https://mvnrepository.com/artifact/androidx.compose.ui
            object UI {

                //  https://mvnrepository.com/artifact/androidx.compose.ui/ui
                const val ui = "androidx.compose.ui:ui:$version"

                //  https://mvnrepository.com/artifact/androidx.compose.ui/ui-tooling
                const val tooling = "androidx.compose.ui:ui-tooling:$version"
            }

            //  https://mvnrepository.com/artifact/androidx.compose.foundation
            object Foundation {

                //  https://mvnrepository.com/artifact/androidx.compose.foundation/foundation
                const val foundation = "androidx.compose.foundation:foundation:$version"
            }

            //  https://mvnrepository.com/artifact/androidx.compose.material
            object Material {

                // https://mvnrepository.com/artifact/androidx.compose.material/material
                const val material = "androidx.compose.material:material:$version"

                //  https://mvnrepository.com/artifact/androidx.compose.material/material-icons-core
                const val iconsCore = "androidx.compose.material:material-icons-core:$version"
            }

            //  https://mvnrepository.com/artifact/androidx.compose.runtime
            object Runtime {

                //  https://mvnrepository.com/artifact/androidx.compose.runtime/runtime
                const val runtime = "androidx.compose.runtime:runtime:$version"

                //  https://mvnrepository.com/artifact/androidx.compose.runtime/runtime-livedata
                const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
            }
        }

        //  https://mvnrepository.com/artifact/androidx.lifecycle
        object Lifecycle {
            private const val version = "2.3.1"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-livedata-core-ktx
            const val coreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-livedata-ktx
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"

            //  https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-viewmodel-ktx
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        //  https://mvnrepository.com/artifact/androidx.test
        object Test {
            private const val version = "1.4.0"

            //  https://mvnrepository.com/artifact/androidx.test/core-ktx
            const val coreKtx = "androidx.test:core-ktx:$version"
        }
    }

    //  https://mvnrepository.com/artifact/com.google.accompanist
    object Accompanist {
        private const val version = "0.16.0"

        //  https://mvnrepository.com/artifact/com.google.accompanist/accompanist-flowlayout
        const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$version"
    }

    //  https://mvnrepository.com/artifact/io.coil-kt
    object Coil {
        private const val version = "1.3.2"

        //  https://mvnrepository.com/artifact/io.coil-kt/coil-compose
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion
    object Hyperion {
        private const val version = "0.9.33"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-core
        const val core = "com.willowtreeapps.hyperion:hyperion-core:$version"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-crash
        const val crash = "com.willowtreeapps.hyperion:hyperion-crash:$version"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-shared-preferences
        const val sharedPreferences = "com.willowtreeapps.hyperion:hyperion-shared-preferences:$version"

        //  https://mvnrepository.com/artifact/com.willowtreeapps.hyperion/hyperion-timber
        const val timber = "com.willowtreeapps.hyperion:hyperion-timber:$version"
    }

    //  https://mvnrepository.com/artifact/io.insert-koin
    object Koin {

        //  https://mvnrepository.com/artifact/io.insert-koin/koin-core-ext
        const val coreExt = "io.insert-koin:koin-core-ext:3.0.2"

        //  https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-ext
        const val androidXExt = "io.insert-koin:koin-androidx-ext:2.2.3"

        //  https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-viewmodel
        const val viewModel = "io.insert-koin:koin-androidx-viewmodel:2.2.3"
    }

    //  https://mvnrepository.com/artifact/com.squareup.moshi
    object Moshi {
        private const val version = "1.12.0"

        //  https://mvnrepository.com/artifact/com.squareup.moshi/moshi-adapters
        const val adapters = "com.squareup.moshi:moshi-adapters:$version"

        //  https://mvnrepository.com/artifact/com.squareup.moshi/moshi-kotlin
        const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
    }

    //  https://mvnrepository.com/artifact/com.squareup.retrofit2
    object Retrofit {
        private const val version = "2.9.0"

        //  https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"

        //  https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-moshi
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
    }

    //  https://mvnrepository.com/artifact/org.robolectric
    object Robolectric {
        private const val version = "4.6.1"

        //  https://mvnrepository.com/artifact/org.robolectric/robolectric
        const val robolectric = "org.robolectric:robolectric:$version"
    }

    //  https://mvnrepository.com/artifact/com.squareup.leakcanary/leakcanary-android
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

    //  https://mvnrepository.com/artifact/com.google.android.material/material
    const val material = "com.google.android.material:material:1.4.0"

    //  https://mvnrepository.com/artifact/com.jakewharton.threetenabp/threetenabp
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.1"

    //  https://mvnrepository.com/artifact/org.threeten/threetenbp
    const val threeTenBp = "org.threeten:threetenbp:1.5.1"

    //  https://mvnrepository.com/artifact/com.jakewharton.timber/timber
    const val timber = "com.jakewharton.timber:timber:4.7.1"

    //  https://mvnrepository.com/artifact/junit/junit
    const val junit = "junit:junit:4.13.2"

    //  https://mvnrepository.com/artifact/io.mockk/mockk
    const val mockK = "io.mockk:mockk:1.12.0"
}
