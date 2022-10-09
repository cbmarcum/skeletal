import uk.co.cacoethes.util.NameType
import org.apache.commons.io.FileUtils

Map props = [:]

if (projectDir.name =~ /\-/) {
    props.project_class_name = transformText(projectDir.name, from: NameType.HYPHENATED, to: NameType.CAMEL_CASE)
} else {
    props.project_class_name = transformText(projectDir.name, from: NameType.PROPERTY, to: NameType.CAMEL_CASE)
}
props.project_name = transformText(props.project_class_name, from: NameType.CAMEL_CASE, to: NameType.HYPHENATED)
props.project_group = ask("Define value for 'group' [org.example]: ", "org.example", "group")
props.project_name = ask("Define value for 'artifactId' [" + props.project_name + "]: ", props.project_name , "artifactId")
props.project_version = ask("Define value for 'version' [0.1.0]: ", "0.1.0", "version")
props.project_package = ask("Define value for 'package' [" + props.project_group + "]: ", props.project_group, "package")
props.project_class_name = ask("Define value for 'className' [" + props.project_class_name + "]: ", props.project_class_name, "className").capitalize()
props.project_property_name = transformText(props.project_class_name, from: NameType.CAMEL_CASE, to: NameType.PROPERTY)
props.project_capitalized_name = props.project_name.capitalize()
String packagePath = props.project_package.replace('.' as char, '/' as char)
props.package_path = packagePath
props.dollar='$'

processTemplates('README.md', props)
processTemplates('app/build.gradle', props)
processTemplates('settings.gradle', props)
processTemplates('app/src/main/groovy/*.groovy', props)
processTemplates('app/src/main/resources/logback.xml', props)
processTemplates('app/src/test/groovy/*.groovy', props)

File mainSources = new File(projectDir, 'app/src/main/groovy')
File testSources = new File(projectDir, 'app/src/test/groovy')

File mainSourcesPath = new File(mainSources, packagePath)
mainSourcesPath.mkdirs()
File testSourcesPath = new File(testSources, packagePath)
testSourcesPath.mkdirs()

def renameFile = { File from, String path ->
    if (from.file) {
        File to = new File(path)
        to.parentFile.mkdirs()
        FileUtils.moveFile(from, to)
    }
}

mainSources.eachFile { File file ->
    renameFile(file, mainSourcesPath.absolutePath + '/' + file.name)
}
testSources.eachFile { File file ->
    renameFile(file, testSourcesPath.absolutePath + '/' + file.name)
}

renameFile(new File(mainSourcesPath, 'App.groovy'), mainSourcesPath.absolutePath + '/' + props.project_class_name + ".groovy")
renameFile(new File(testSourcesPath, 'AppSpec.groovy'), testSourcesPath.absolutePath + '/' + props.project_class_name + "Spec" + ".groovy")
