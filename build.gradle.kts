plugins {
    val kotlinVersion = "1.5.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.json:json:20220320")
    implementation("com.alibaba:fastjson:2.0.3.graal")
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
//    maven("https://repo1.maven.org/maven2/com/alibaba/fastjson")
    maven {url = uri("https://repo1.maven.org/maven2/com/alibaba/fastjson")}
    mavenCentral()
}

