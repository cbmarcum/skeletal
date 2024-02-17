package uk.co.cacoethes.lazybones.packagesources

import org.jfrog.artifactory.client.Artifactory
import static org.jfrog.artifactory.client.ArtifactoryClientBuilder.create

class DefaultArtifactoryClientFactory implements ArtifactoryClientFactory {
     Artifactory createClient(ArtifactoryPackageSource.ArtifactoryConfig configuration) {
        return create()
                .setUrl(configuration.url)
                .setUsername(configuration.username)
                .setPassword(configuration.password)
                .build()
    }
}

interface ArtifactoryClientFactory {
     Artifactory createClient(ArtifactoryPackageSource.ArtifactoryConfig configuration)
}
