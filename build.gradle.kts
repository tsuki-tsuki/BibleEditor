import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

group = "io.github.tsuki"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    // File picker
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")

    // Serialization
    implementation("io.github.pdvrieze.xmlutil:core:0.86.3")
    implementation("io.github.pdvrieze.xmlutil:serialization:0.90.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            windows {
                includeAllModules = true
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "BibleEditor"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            obfuscate = false
            optimize = false
            version = "7.6.0"
        }
    }
}
