rootProject.name = "jasperreports-gradle-plugin-sample"

pluginManagement {
    repositories{
        // Solo usado para desarrollo local
        mavenLocal()

        // Mientras el plugin no tenga una versión estable, estará hospedado
        // como SNAPHOST. Este repositorio debe aparecer en build y settings.
        maven("https://oss.sonatype.org/content/repositories/snapshots/")

        // Necesario para usar los plugins generales como el compilador de
        // Java y Kotlin
        gradlePluginPortal()
    }
}