plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

val aocMainClass = "aoc.MainKt"

group = "aoc"
version = "1.0-SNAPSHOT"

application {
    mainClass.set(aocMainClass)
}

kotlin.sourceSets {
    main { kotlin.srcDir("src") }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
}
