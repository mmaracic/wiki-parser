
plugins {
    java
    // NOTE: external plugin version is specified in implementation dependency artifact of the project's build file
    id("io.spring.dependency-management")
    //applies spring BOM to define compatible version for spring related library
    id("java-test-fixtures")
}

repositories {
    mavenCentral()
}

//Needs to be here otherwise versions won't transfer to modules that do not apply org.springframework.boot plugin (libraries)
dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    compileOnly("org.hibernate.orm:hibernate-jpamodelgen")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Implementation dependencies are not leaked to consumers when building
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
}
