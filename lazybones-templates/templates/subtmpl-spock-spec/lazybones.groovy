import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine

import static org.apache.commons.io.FilenameUtils.concat

registerEngine "hbs", new HandlebarsTemplateEngine()
clearDefaultEngine()

Map props = [:]
// Pass in parameters from the project template
String parentPackage = parentParams.package
props.parentClassName = parentParams.className

props.pkg = ask("Define value 'package' [" + parentPackage + "]: ", parentPackage, "package")
props.cls = ask("Define value for 'class' name: [SimpleSpec]", "SimpleSpec", "class").capitalize()

processTemplates("**/*", props)

String pkgPath = props.pkg.replace('.' as char, '/' as char)
String filename = props.cls.capitalize() + ".groovy"
File destFile = new File(projectDir, concat(concat("app/src/test/groovy", pkgPath), filename))
destFile.parentFile.mkdirs()

FileUtils.moveFile(new File(templateDir, "Spec.groovy"), destFile)

println "Created new Spock Specification ${FilenameUtils.normalize(destFile.path)}"
