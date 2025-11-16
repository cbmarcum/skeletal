@Grab(group="net.codebuilders", module="groovy-handlebars-engine", version="0.3.0-M1")
import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine

def hbsEngine = new HandlebarsTemplateEngine()
registerEngine "hbs", hbsEngine
registerDefaultEngine hbsEngine

def model = [foo: "test", bar: 100]
processTemplates "**/*Hello.groovy", model
