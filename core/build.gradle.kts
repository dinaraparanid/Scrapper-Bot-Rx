plugins {
    id("java")
    kotlin("jvm")
}

group = "com.paranid5"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}

kotlin {
    jvmToolchain(21)
}
