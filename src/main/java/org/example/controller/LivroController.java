package org.example.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.example.model.Livro;
import org.example.service.LivroService;

import java.util.Map;

public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    public void registerRoutes(Javalin app) {
        app.get("/", this::listarTodos);
        app.get("/add", this::exibirFormularioAdicionar);
        app.post("/add-book", this::adicionarLivro);
        app.get("/edit/{id}", this::exibirFormularioEditar);
        app.post("/update-book", this::atualizarLivro);
        app.post("/delete/{id}", this::removerLivro);
    }

    public void listarTodos(Context ctx) {
        ctx.render("templates/list-books.html", Map.of("livros", livroService.listar()));
    }

    public void exibirFormularioAdicionar(Context ctx) {
        ctx.render("templates/add-book.html");
    }

    public void adicionarLivro(Context ctx) {
        Livro novoLivro = criarLivroDoFormulario(ctx, 0);
        livroService.adicionar(novoLivro);
        ctx.redirect("/");
    }

    public void exibirFormularioEditar(Context ctx) {
        int id = obterIdDoPath(ctx);
        Livro livro = livroService.buscarPorId(id)
                .orElseThrow(() -> new NotFoundResponse("Livro não encontrado"));
        ctx.render("templates/edit-book.html", Map.of("livro", livro));
    }

    public void atualizarLivro(Context ctx) {
        int id = Integer.parseInt(ctx.formParam("id"));
        Livro livroAtualizado = criarLivroDoFormulario(ctx, id);

        livroService.atualizar(livroAtualizado);
        ctx.redirect("/");
    }

    public void removerLivro(Context ctx) {
        int id = obterIdDoPath(ctx);
        livroService.remover(id);
        ctx.redirect("/");
    }


    private Livro criarLivroDoFormulario(Context ctx, int id) {
        String titulo = ctx.formParam("titulo");
        String autor = ctx.formParam("autor");
        String anoStr = ctx.formParam("anoPublicacao");

        int ano = (anoStr != null && !anoStr.isBlank()) ? Integer.parseInt(anoStr) : 0;

        return new Livro(id, titulo, autor, ano);
    }

    private int obterIdDoPath(Context ctx) {
        return Integer.parseInt(ctx.pathParam("id"));
    }
}