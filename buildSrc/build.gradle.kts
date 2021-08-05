import org.jetbrains.kotlin.konan.properties.loadProperties

plugins { `kotlin-dsl` }

repositories {
    mavenCentral()
    google()
}

val properties = loadProperties(File(projectDir.parent, "gradle.properties").path)
val agpVersion = properties["agpVersion"].toString()
val kotlinVersion = properties["kotlin.version"].toString()

System.setProperty("agpVersion", agpVersion)
System.setProperty("kotlinVersion", kotlinVersion)

dependencies {
    implementation("com.android.tools.build:gradle:$agpVersion")
    implementation(kotlin("gradle-plugin", version = kotlinVersion))
    implementation(kotlin("stdlib", version = kotlinVersion))
}
