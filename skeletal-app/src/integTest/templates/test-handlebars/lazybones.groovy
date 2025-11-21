import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine

registerEngine "hbs", new HandlebarsTemplateEngine()

def model = [foo: "test", bar: 100]
processTemplates "**/*Hello.groovy", model
