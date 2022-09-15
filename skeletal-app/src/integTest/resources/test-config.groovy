bintrayRepositories = ["pledbrook/lazybones-templates"]
simpleRepositories = ["https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates",
                      "https://codebuilders.jfrog.io/artifactory/generic/openoffice-templates"]

templates {
    mappings {
        customRatpack = "http://dl.dropboxusercontent.com/u/29802534/custom-ratpack.zip"
        doesNotExist = "file:///does/not/exist"
        customAooAddin = "https://codebuilders.jfrog.io/artifactory/generic/openoffice-templates/aoo-addin-template-0.3.0.zip"
    }
}

test.option.override = "Just an option"
