// config.file = new File(System.getProperty('user.home'), '.lazybones/config.groovy').path
// cache.dir = new File(System.getProperty('user.home'), ".lazybones/templates").path

// bintrayRepositories = ["pledbrook/lazybones-templates"]

config.file = new File(System.getProperty('user.home'), '.skeletal/config.groovy').path
cache.dir = new File(System.getProperty('user.home'), ".skeletal/templates").path
simpleRepositories = [
        "https://skeletal.s3.us-east-2.amazonaws.com/default-templates"
]
