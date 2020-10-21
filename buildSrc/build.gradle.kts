import org.jetbrains.kotlin.konan.properties.loadProperties

plugins { `kotlin-dsl` }

repositories {
    jcenter()
    google()
}

val properties = loadProperties(File(projectDir.parent, "gradle.properties").path)
val ideVersion = properties["android.ideVersion"].toString()
val kotlinVersion = properties["kotlin.version"].toString()

System.setProperty("kotlinVersion", kotlinVersion)
System.setProperty("ideVersion", ideVersion)

dependencies {
    implementation("com.android.tools.build:gradle:$ideVersion")
    implementation(kotlin("gradle-plugin", version = kotlinVersion))
}
