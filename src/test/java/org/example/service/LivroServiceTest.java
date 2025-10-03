package org.example.service;

import org.example.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LivroServiceTest {
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = new LivroService();
    }

    @Test
    @DisplayName("Happy path: deve adicionar com sucesso um livro válido")
    void deveAdicionarUmLivroComSucesso() {
        int id = livroService.gerarId();
        Livro livro = new Livro(id, "The Catcher in the Rye", "J.D. Salinger", 1951);

        livroService.adicionar(livro);

        Livro livroAdicionado = livroService.buscar(id);
        Collection<Livro> todosOsLivros = livroService.listar();

        assertNotNull(livroAdicionado, "O livro não deveria ser nulo após ser adicionado.");
        assertEquals(1, todosOsLivros.size(), "Deveria haver exatamente um livro no repositório.");
        assertEquals("The Catcher in the Rye", livroAdicionado.getTitulo(), "O título do livro não corresponde ao esperado.");
        assertEquals(id, livroAdicionado.getId(), "O ID do livro não corresponde ao esperado.");
    }

    @Test
    @DisplayName("Buscar Livro: deve lançar exceção ao buscar um ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            livroService.buscar(1);
        });

        String mensagemEsperada = "Livro não encontrado";
        String mensagemAtual = exception.getMessage();
        assertTrue(mensagemAtual.contains(mensagemEsperada), "A mensagem de erro não é a esperada.");
    }

    @Test
    @DisplayName("Listar Vazio: deve retornar uma lista vazia quando nenhum livro for adicionado")
    void deveRetornarListaVaziaQuandoNaoHouverLivros() {

        Collection<Livro> livros = livroService.listar();

        assertNotNull(livros, "A lista não deve ser nula.");
        assertTrue(livros.isEmpty(), "A lista de livros deveria estar vazia.");
    }

    @Test
    @DisplayName("Happy Path Listar: deve retornar a lista de livros existentes")
    void deveExibirOsLivrosExistentes() {
        Livro livro1 = new Livro(livroService.gerarId(), "Lost Horizon", "James Hilton", 1933);
        Livro livro2 = new Livro(livroService.gerarId(), "Distant Star", "Roberto Bolaño", 1996);

        livroService.adicionar(livro1);
        livroService.adicionar(livro2);

        Collection<Livro> livros = livroService.listar();

        assertNotNull(livros);
        assertEquals(2, livros.size(), "A lista deveria conter dois livros.");
        assertTrue(livros.contains(livro1), "A lista deveria conter o livro 'Lost Horizon'.");
        assertTrue(livros.contains(livro2), "A lista deveria conter o livro 'Distant Star'.");
    }

    @Test
    @DisplayName("Happy Path Remover: deve remover o livro com sucesso")
    void deveRemoverUmLivroComSucesso() {

        Livro livroRemover = new Livro(livroService.gerarId(), "Lost Horizon", "James Hilton", 1933);
        livroService.adicionar(livroRemover);
        int id = livroRemover.getId();

        livroService.remover(id);

        assertThrows(NoSuchElementException.class, () -> {
            livroService.buscar(id);
        });

        assertTrue(livroService.listar().isEmpty());
    }

    @Test
    @DisplayName("ID Inválido Remover: deve falhar ao tentar remover livro com ID inexistente")
    void deveLancarExcecaoAoTentarRemoverLivroInexistente() {

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            livroService.remover(1);
        });

        assertEquals("Livro não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Happy Path Atualizar: deve atualizar o livro com sucesso")
    void deveAtualizarUmLivroComSucesso() {
        Livro livroOriginal = new Livro(livroService.gerarId(), "Lost Horizon", "James Hilton", 2025);
        livroService.adicionar(livroOriginal);
        int id = livroOriginal.getId();

        Livro livroAtualizado = new Livro(id, "Lost Horizon", "James Hilton", 1933);

        livroService.atualizar(livroAtualizado);

        Livro livroDoRepositorio = livroService.buscar(id);

        assertNotNull(livroDoRepositorio);
        assertEquals(1933, livroDoRepositorio.getAnoPublicacao());
        assertEquals(id, livroDoRepositorio.getId());
    }

    @Test
    @DisplayName("Atualizar ID incorreto: deve falhar ao tentar atualizar livro com ID inexistente")
    void deveLancarExcecaoAoTentarAtualizarLivroInexistente() {
        int idInexistente = 666;
        Livro livroInexistente = new Livro(idInexistente, "Nada", "Ninguém", 2001);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            livroService.atualizar(livroInexistente);
        });

        assertEquals("Livro não encontrado", exception.getMessage());
    }
}