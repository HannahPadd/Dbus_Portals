import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
}

group = "dev.hannahpadd"
version = "0.1.0"

repositories {
    mavenCentral()
}

val dbusTools: Configuration by configurations.creating

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("com.github.hypfvieh:dbus-java-core:5.2.0")
    implementation("com.github.hypfvieh:dbus-java-transport-jnr-unixsocket:5.2.0")
    dbusTools("com.github.hypfvieh:dbus-java-utils:5.2.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.set(listOf("-Xvalue-classes"))
    }
}

// Set compiler to use UTF-8
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}


gradle.taskGraph.whenReady {
    tasks.named<Test>("test") {
        onlyIf {
            // Run tests only if the 'test' task is invoked directly
            this@whenReady.hasTask(":test") && !this@whenReady.hasTask(":build") && !this@whenReady.hasTask(":assemble")
        }
        useJUnitPlatform()
    }
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.hannahpadd"
            artifactId = "dbusglobalshortcutskotlin"
            version = "0.1.0"

            from(components["java"])
        }
    }
}

tasks.register<JavaExec>("generateDbusInterface") {
    group = "build"
    mainClass.set("org.freedesktop.dbus.utils.generator.InterfaceCodeGenerator")
    classpath = dbusTools

    doFirst {
        file("src/main/kotlin/generated").mkdirs()
    }

    args = listOf(
        "--session",
        "--outputDir", "src/main/kotlin",
        "--all",
        "org.freedesktop.portal.Desktop",
        "/org/freedesktop/portal/desktop"
    )
}