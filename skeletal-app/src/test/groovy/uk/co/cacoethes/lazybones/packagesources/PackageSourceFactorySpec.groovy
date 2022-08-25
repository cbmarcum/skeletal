package uk.co.cacoethes.lazybones.packagesources

import spock.lang.Specification
import uk.co.cacoethes.lazybones.config.Configuration

class PackageSourceFactorySpec extends Specification {
    PackageSourceBuilder packageSourceFactory

    final static expectedSimpleRepositories = ['repo1', 'repo2', 'repo3']

    void 'returns a list of Simple package sources from the ConfigObject'() {
        given: 'A package source factory'
        packageSourceFactory = new PackageSourceBuilder()

        when: 'the package name doesn\'t start with http://'
        List<PackageSource> packageSources = packageSourceFactory.buildPackageSourceList(
                initConfig(simpleRepositories: expectedSimpleRepositories))

        then: 'there is a SimplePackageSource for each of the expectedSimpleRepositories'
        packageSources.collect { it.repoName } == expectedSimpleRepositories
    }

    protected Configuration initConfig(Map settings) {
        return new Configuration(
                new ConfigObject(),
                settings,
                [:],
                ["simpleRepositories": String[]],
                new File("delete-me.json"))
    }
}
