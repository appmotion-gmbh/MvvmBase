// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(group = "com.android.tools.build", name = "gradle", version = Versions.gradleTools)
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHilt}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    kotlin("jvm") version Versions.kotlin
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        android.set(true)
        // disable lexicographic import ordering rule
        disabledRules.set(listOf("import-ordering", "indent"))
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        buildUponDefaultConfig = true
        config = files(
            when (name) {
                "sample" -> projectDir.canonicalPath
                else -> project.rootDir.canonicalPath
            } + File.separator + "detekt-config.yml"
        )
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

Publishing.setupSigning(project)
