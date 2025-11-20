package org.example;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.config.Database;
import org.example.controller.LivroController;
import org.example.service.LivroService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class Main {

    public static void main(String[] args) {
        Database.initialize();

        Javalin app = Javalin.create(config -> {
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
            config.fileRenderer(new JavalinThymeleaf(templateEngine));
        }).start(7070);

        LivroService livroService = new LivroService();
        LivroController livroController = new LivroController(livroService);

        livroController.registerRoutes(app);
    }
}