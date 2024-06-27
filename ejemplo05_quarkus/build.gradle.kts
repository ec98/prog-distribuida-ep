plugins {
    id("java")
    id("io.quarkus") version "3.11.1"
}

group = "com.distribuida"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
val quarkusVersion = "3.11.1"

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}")) //APLICA TODO
    implementation("io.quarkus:quarkus-arc") //CDI
    //motor jackRs (reactiva)
    implementation("io.quarkus:quarkus-resteasy-reactive") //REST
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson") //JSON para gestionar archivos tipo json

    //generar base de datos
    implementation("io.quarkus:quarkus-hibernate-orm-panache") //JPA: Hibernate
//    implementation("com.h2database:h2:2.2.224")

//    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("io.quarkus:quarkus-jdbc-postgresql:3.11.2")

    //REST CLIENT
    implementation("io.quarkus:quarkus-rest-client-reactive")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")

    //Load Balancer
    implementation("io.smallrye.stork:stork-service-discovery-static-list:2.6.0")

}