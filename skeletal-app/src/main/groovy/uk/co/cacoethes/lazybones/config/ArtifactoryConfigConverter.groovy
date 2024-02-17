package uk.co.cacoethes.lazybones.config

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import uk.co.cacoethes.lazybones.packagesources.ArtifactoryPackageSource

/**
 * Config converter for artifactory properties
 */
class ArtifactoryConfigConverter implements Converter<ArtifactoryPackageSource.ArtifactoryConfig> {

    private slurper = new JsonSlurper()

    @Override
    ArtifactoryPackageSource.ArtifactoryConfig toType(String value) {
        return slurper.parseText(value) as ArtifactoryPackageSource.ArtifactoryConfig
    }

    @Override
    String toString(ArtifactoryPackageSource.ArtifactoryConfig value) {
        return JsonOutput.toJson(value)
    }

    @Override
    boolean validate(Object value) {
        return true
    }
}
