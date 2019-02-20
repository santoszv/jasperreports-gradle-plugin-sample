import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"

    // Jasper Reports Gradle Plugin
    id("mx.com.inftel.jasperreports") version "1.0-SNAPSHOT"

    war
}

repositories {
    // Solo usado para desarrollo local
    //maven("repo")

    // Mientras el plugin no tenga una versión estable, estará hospedado
    // como SNAPHOST. Este repositorio debe aparecer en build y settings.
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    // Jasper Reports esta en el repositorio central, pero algunas de sus
    // dependencias no lo están, es necesario tener ambos repositorios.
    mavenCentral()
    maven("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    // La dependencia de Jasper Report debe ser declarada tanto para el
    // proyecto (compile) como para el plugin (jasperreports).
    compile("net.sf.jasperreports:jasperreports:6.7.0")
    jasperreports("net.sf.jasperreports:jasperreports:6.7.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

jasperreports {
    // Directorio con los archivos .jrxml
    // Por defecto: src/main/jasperreports
    inputReportsDir = "src/main/jasperreports"
    // Directorio para los archivos .jasper
    // Por defecto: build/jasperreports
    outputReportsDir = "build/jasperreports"
    // Nombre del conjunto de código fuente, por lo general es main.
    // Por defecto: main
    sourceSet = "main"
}

// Para compilar el reporte, primero es necesario compilar el código
// Java y Kotlin, por lo tanto, la tarea de Jasper Reports dependerá
// de la compilación. A su vez, la tarea de empaquetado debe depender
// de los reportes.

val taskCompileJava = tasks.getByName("compileJava")
val taskCompileKotlin = tasks.getByName("compileKotlin")
val taskJasperReports = tasks.getByName("jasperreportsCompileReports")
//val taskJar = tasks.getByName("jar")
val taskWar = tasks.getByName("war")

taskJasperReports.dependsOn(taskCompileJava)
taskJasperReports.dependsOn(taskCompileKotlin)
//taskJar.dependsOn(taskJasperReports)
taskWar.dependsOn(taskJasperReports)

// Los reportes compilados deben también ser empaquetados, la tarea jar
// es la responsable de empaquetar, por lo tanto, hay que reconfigurar.

//tasks.withType<Jar> {
//    from("build/jasperreports") {
//        include("**/*.jasper")
//    }
//}

tasks.withType<War> {
    from("build/jasperreports") {
        into("WEB-INF/classes")
        include("**/*.jasper")
    }
}
