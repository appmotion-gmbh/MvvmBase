plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.jfrog.bintray")
    `maven-publish`
}

android {
    compileSdkVersion(Android.compileSdk)

    defaultConfig {
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.compileSdk)
        versionCode = Publishing.versionCode
        versionName = Publishing.versionName

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        named("release").configure {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }

    dataBinding {
        // This is necessary to allow the data binding annotation processor to generate
        // the BR fields from Bindable annotations
        testOptions.unitTests.isIncludeAndroidResources = true

        isEnabledForTests = true
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    // Support library
    implementation("androidx.appcompat:appcompat:1.1.0")

    testAnnotationProcessor("androidx.databinding:databinding-compiler:$gradleToolsVersion")
    kaptTest("androidx.databinding:databinding-compiler:$gradleToolsVersion")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // Lifecycle architecture components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    api("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0")

    // Java inject API for dependency injection
    api("javax.inject:javax.inject:1")
}

repositories {
    mavenCentral()
    google()
}

val sourcesJar = task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.sourceFiles)
}

task<Javadoc>("javadoc") {
    source = android.sourceSets["main"].java.sourceFiles
    classpath += project.files(android.bootClasspath.joinToString(separator = File.pathSeparator))
}

artifacts.archives(sourcesJar)

// Bintray
bintray.apply {
    user = Publishing.getBintrayUser(rootProject)
    key = Publishing.getBintrayApiKey(rootProject)

    setConfigurations("archives")
    pkg.apply {
        repo = Publishing.groupId
        name = Publishing.mainArtifactId
        desc = Publishing.mainDescription
        websiteUrl = Publishing.url
        vcsUrl = Publishing.gitUrl
        setLicenses("Apache-2.0")
        publish = true
        publicDownloadNumbers = true
        version.apply {
            name = Publishing.versionName
            desc = Publishing.mainDescription
            gpg.apply {
                sign = true
                passphrase = Publishing.getBintrayGpgPassphrase(rootProject)
            }
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = Publishing.groupId
                artifactId = Publishing.mainArtifactId
                version = Publishing.versionName

                pom {
                    packaging = "aar"

                    name.set(Publishing.mainArtifactId)
                    description.set(Publishing.mainDescription)
                    url.set(Publishing.url)

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set(Publishing.licenseUrl)
                        }
                    }

                    scm {
                        connection.set(Publishing.gitUrl)
                        developerConnection.set(Publishing.gitUrl)
                        url.set(Publishing.url)
                    }
                }
            }
        }
    }
}