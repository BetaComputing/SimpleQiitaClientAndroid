import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("com.github.ben-manes.versions") version "0.33.0"
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Project.ideVersion}")
        classpath(kotlin("gradle-plugin", version = Project.kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        android.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        disabledRules.set(listOf("import-ordering", "no-wildcard-imports"))
        reporters { reporter(ReporterType.CHECKSTYLE) }
    }
}

subprojects {
    val isAppModule = (name == "app")

    apply(plugin = if (isAppModule) "com.android.application" else "com.android.library")
    apply(plugin = "org.jetbrains.kotlin.android")
    apply(plugin = "org.jetbrains.kotlin.android.extensions")

    android {
        compileSdkVersion(Project.compileSdkVersion)
        buildToolsVersion(Project.buildToolsVersion)

        defaultConfig {
            if (isAppModule) applicationId = Project.applicationId
            targetSdkVersion(Project.targetSdkVersion)
            minSdkVersion(Project.minSdkVersion)
            versionCode = Project.versionCode
            versionName = Project.versionName
        }

        buildFeatures.dataBinding = true
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

        lintOptions {
            xmlReport = true
            isAbortOnError = false
            setDisable(setOf("ObsoleteLintCustomCheck"))
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
