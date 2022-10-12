// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application").version("7.3.0") apply false
    id ("com.android.library").version("7.3.0") apply false
    id ("org.jetbrains.kotlin.android").version("1.7.10") apply false
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
}

detekt {
    source = files("$projectDir")
    config = files("config/detekt.yml")
    buildUponDefaultConfig = true
    autoCorrect = true
}