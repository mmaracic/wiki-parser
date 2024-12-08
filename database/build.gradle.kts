/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("wiki.parser.java-library-conventions")
    id("wiki.parser.spring-common-conventions")
}

dependencies {

    implementation(project(":core"))

    //api means this dependency is exposed to modules using this one
    //Specifically we need it in app module to enable jpa repositories
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    //ObjectMapper
    implementation("org.springframework.boot:spring-boot-starter-json")

    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //liquibase
    implementation("org.liquibase:liquibase-core")

    //postgres testcontainer
    testFixturesApi("org.testcontainers:postgresql:1.20.1")
    //postgres driver
    testFixturesApi("org.postgresql:postgresql")

}
