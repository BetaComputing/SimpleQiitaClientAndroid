import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BuildType
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun org.gradle.api.Project.android(action: BaseExtension.() -> Unit) {
    val extension = extensions.getByName("android") as? BaseExtension
    if (extension != null) action.invoke(extension)
}

var BuildFeatures.dataBinding: Boolean?
    get() = when (this) {
        is ApplicationBuildFeatures -> dataBinding
        is LibraryBuildFeatures -> dataBinding
        else -> null
    }
    set(value) = when (this) {
        is ApplicationBuildFeatures -> dataBinding = value
        is LibraryBuildFeatures -> dataBinding = value
        else -> Unit
    }

fun NamedDomainObjectContainer<BuildType>.debug(body: BuildType.() -> Unit) {
    getByName("debug") { body(this) }
}

fun NamedDomainObjectContainer<BuildType>.release(body: BuildType.() -> Unit) {
    getByName("release") { body(this) }
}

fun BaseExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

}

val NamedDomainObjectContainer<out AndroidSourceSet>.main: AndroidSourceSet get() = getByName("main")

val NamedDomainObjectContainer<out AndroidSourceSet>.test: AndroidSourceSet get() = getByName("test")

val NamedDomainObjectContainer<out AndroidSourceSet>.androidTest: AndroidSourceSet get() = getByName("androidTest")
