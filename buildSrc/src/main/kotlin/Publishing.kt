import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

object Publishing {
    const val versionName = "3.0.5"

    const val url = "https://github.com/appmotion-gmbh/MvvmBase"
    const val gitUrl = "https://github.com/appmotion-gmbh/MvvmBase.git"
    const val licenseUrl = "https://github.com/appmotion-gmbh/MvvmBase/blob/master/LICENSE"
    const val groupId = "io.github.appmotion-gmbh"
    private val properties = Properties()

    fun getOssrhUsername() = properties["private_ossrh_user"]?.toString()
    fun getOssrhPassword() = properties["private_ossrh_password"]?.toString()

    fun setupSigning(project: Project) {
        properties.load(FileInputStream(project.rootProject.file("local.properties")))
        project.rootProject.extra["signing.keyId"] = properties["private_ossrh_signing_keyid"]?.toString()
        project.rootProject.extra["signing.password"] = properties["private_ossrh_signing_passphrase"]?.toString()
        project.rootProject.extra["signing.secretKeyRingFile"] = Paths.get(project.rootDir.canonicalPath, "signing.gpg")
    }
}

fun PublicationContainer.create(publication: Publication, project: Project) = create<MavenPublication>("release") {
    from(project.components["release"])

    project.getTasksByName("sourcesJar", false).forEach { artifact(it) }

    groupId = Publishing.groupId
    artifactId = publication.artifactId
    version = Publishing.versionName

    pom {
        packaging = "aar"

        name.set(publication.artifactId)
        description.set(publication.description)
        url.set(Publishing.url)

        developers {
            developer {
                id.set("Appmotion")
                name.set("Lucas Krug")
                email.set("entwicklung@appmotion.de")
            }
        }

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set(Publishing.licenseUrl)
            }
        }

        scm {
            connection.set(Publishing.gitUrl)
            developerConnection.set(Publishing.gitUrl)
            url.set(Publishing.url)
        }
    }
}