import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine

def hbsEngine = new HandlebarsTemplateEngine()
registerEngine "hbs", hbsEngine
registerDefaultEngine hbsEngine

def model = [foo: "test", bar: 100]
processTemplates "**/*Hello.groovy", model
