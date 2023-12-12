import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

fun RepositoryHandler.mavenCentralUpload(): MavenArtifactRepository = maven {
    val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    name = "Sonatype"
    setUrl(releaseRepo)
    credentials {
        username = Publishing.getOssrhUsername()
        password = Publishing.getOssrhPassword()
    }
}