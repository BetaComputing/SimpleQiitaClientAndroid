import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("com.github.ben-manes.versions") version "0.39.0"
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Project.agpVersion}")
        classpath(kotlin("gradle-plugin", version = Project.kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        android.set(true)
        outputColorName.set("RED")
        disabledRules.set(listOf("import-ordering", "no-wildcard-imports"))
        reporters { reporter(ReporterType.CHECKSTYLE) }
    }
}

subprojects {
    val isAppModule = (name == "app")

    apply(plugin = if (isAppModule) "com.android.application" else "com.android.library")
    apply(plugin = "org.jetbrains.kotlin.android")

    android {
        compileSdkVersion(Project.compileSdkVersion)
        buildToolsVersion(Project.buildToolsVersion)

        defaultConfig {
            if (isAppModule) applicationId = Project.applicationId
            targetSdk = Project.targetSdkVersion
            minSdk = Project.minSdkVersion
            versionCode = Project.versionCode
            versionName = Project.versionName
        }

        buildFeatures.viewBinding = true
        buildFeatures.dataBinding = true
        buildFeatures.compose = true
        testOptions.unitTests.isIncludeAndroidResources = true

        sourceSets {
            main.java.srcDirs("src/main/kotlin")
            test.java.srcDirs("src/test/kotlin")
            androidTest.java.srcDirs("src/androidTest/kotlin")
        }

        buildTypes {
            debug {
                isMinifyEnabled = false
            }

            release {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        composeOptions {
            kotlinCompilerExtensionVersion = Project.composeVersion
        }

        lintOptions {
            xmlReport = true
            isAbortOnError = true
            setDisable(setOf("ObsoleteLintCustomCheck", "MediaCapabilities"))
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class).configure {
    fun String.isStable() = Regex("^[0-9,.v-]+(-r)?$").matches(this)
    fun String.isNotStable() = this.isStable().not()

    rejectVersionIf {
        currentVersion.isStable() && candidate.version.isNotStable()
    }
}
