import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import static org.apache.commons.io.FilenameUtils.concat

Map props = [:]
// Pass in parameters from the project template
String parentPackage = parentParams.package
props.parentClassName = parentParams.className

props.pkg = ask("Define value 'package' [" + parentPackage + "]: ", parentPackage, "package")
props.cls = ask("Define value for 'class' name: [SimpleSpec]", "SimpleSpec", "class").capitalize()

processTemplates("Spec.groovy", props)

String pkgPath = props.pkg.replace('.' as char, '/' as char)
String filename = props.cls.capitalize() + ".groovy"
File destFile = new File(projectDir, concat(concat("app/src/test/groovy", pkgPath), filename))
destFile.parentFile.mkdirs()

FileUtils.moveFile(new File(templateDir, "Spec.groovy"), destFile)

println "Created new Spock Test ${FilenameUtils.normalize(destFile.path)}"
