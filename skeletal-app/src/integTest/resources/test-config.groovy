bintrayRepositories = ["pledbrook/lazybones-templates"]
simpleRepositories = ["https://skeletal.s3.us-east-2.amazonaws.com/default-templates",
                      "https://skeletal.s3.us-east-2.amazonaws.com/openoffice-templates"]

templates {
    mappings {
        customRatpack = "http://dl.dropboxusercontent.com/u/29802534/custom-ratpack.zip"
        doesNotExist = "file:///does/not/exist"
        customAooAddin = "https://skeletal.s3.us-east-2.amazonaws.com/openoffice-templates/aoo-addin-template-0.3.0.zip"
    }
}

test.option.override = "Just an option"
