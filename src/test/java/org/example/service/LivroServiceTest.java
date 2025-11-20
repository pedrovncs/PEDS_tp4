package org.example.service;

import org.example.model.Livro;
import org.example.config.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = new LivroService();
        limparTabelaLivros();
    }

    @AfterEach
    void tearDown() {
        limparTabelaLivros();
    }

    private void limparTabelaLivros() {
        String sql = "DELETE FROM livros;";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao limpar tabela livros: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("deve adicionar um livro com sucesso")
    void deveAdicionarUmLivroComSucesso() {
        Livro novoLivro = new Livro(0, "O Apanhador no Campo de Centeio", "J.D. Salinger", 1951);
        livroService.adicionar(novoLivro);

        Collection<Livro> todosOsLivros = livroService.listar();
        Livro livroAdicionado = todosOsLivros.iterator().next();

        assertNotNull(livroAdicionado, "O livro não deveria ser nulo após ser adicionado.");
        assertEquals(1, todosOsLivros.size(), "Deveria haver exatamente um livro na tabela.");
        assertEquals("O Apanhador no Campo de Centeio", livroAdicionado.getTitulo());
        assertTrue(livroAdicionado.getId() > 0, "O ID do livro deveria ter sido gerado pelo banco de dados.");
    }

    @Test
    @DisplayName("deve retornar Optional vazio ao buscar ID inexistente")
    void deveRetornarOptionalVazioAoBuscarIdInexistente() {
        Optional<Livro> resultado = livroService.buscarPorId(999);

        assertNotNull(resultado, "O Optional não deve ser nulo.");
        assertTrue(resultado.isEmpty(), "O Optional deveria estar vazio para um ID inexistente.");
    }

    @Test
    @DisplayName("deve retornar uma lista vazia quando nenhum livro for adicionado")
    void deveRetornarListaVaziaQuandoNaoHouverLivros() {
        Collection<Livro> livros = livroService.listar();
        assertNotNull(livros, "A lista não deve ser nula.");
        assertTrue(livros.isEmpty(), "A lista de livros deveria estar vazia.");
    }

    @Test
    @DisplayName("deve retornar todos os livros existentes")
    void deveRetornarTodosOsLivrosExistentes() {
        Livro livro1 = new Livro(0, "Horizonte Perdido", "James Hilton", 1933);
        Livro livro2 = new Livro(0, "Estrela Distante", "Roberto Bolaño", 1996);

        livroService.adicionar(livro1);
        livroService.adicionar(livro2);

        Collection<Livro> livros = livroService.listar();

        assertNotNull(livros);
        assertEquals(2, livros.size(), "A lista deveria conter dois livros.");
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Horizonte Perdido")));
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Estrela Distante")));
    }

    @Test
    @DisplayName("deve remover um livro com sucesso")
    void deveRemoverUmLivroComSucesso() {
        Livro livroParaRemover = new Livro(0, "O Sol é para Todos", "Harper Lee", 1960);
        livroService.adicionar(livroParaRemover);

        int idParaRemover = livroService.listar().iterator().next().getId();

        livroService.remover(idParaRemover);

        Optional<Livro> resultado = livroService.buscarPorId(idParaRemover);
        assertTrue(resultado.isEmpty(), "O livro não deveria ser encontrado após a remoção.");
        assertTrue(livroService.listar().isEmpty(), "A lista de livros deveria estar vazia após a remoção.");
    }

    @Test
    @DisplayName("não deve falhar ao tentar remover um ID inexistente")
    void naoDeveFalharAoRemoverIdInexistente() {
        livroService.adicionar(new Livro(0, "1984", "George Orwell", 1949));

        assertDoesNotThrow(() -> {
            livroService.remover(999);
        });

        assertEquals(1, livroService.listar().size(), "A lista de livros não deveria ter sido alterada.");
    }

    @Test
    @DisplayName("deve atualizar os dados de um livro com sucesso")
    void deveAtualizarUmLivroComSucesso() {
        livroService.adicionar(new Livro(0, "O Grande Gatsby", "F. Scott Fitzgerald", 1925));

        Livro livroOriginal = livroService.listar().iterator().next();
        int idDoLivro = livroOriginal.getId();

        Livro livroAtualizado = new Livro(idDoLivro, "O Grande Gatsby", "F. Scott Fitzgerald", 1926);

        livroService.atualizar(livroAtualizado);

        Optional<Livro> livroDoBanco = livroService.buscarPorId(idDoLivro);

        assertTrue(livroDoBanco.isPresent(), "O livro deveria ser encontrado.");
        assertEquals(1926, livroDoBanco.get().getAnoPublicacao(), "O ano de publicação deveria ter sido atualizado.");
        assertEquals("O Grande Gatsby", livroDoBanco.get().getTitulo(), "O título não deveria ter mudado.");
    }

    @Test
    @DisplayName("não deve falhar ao tentar atualizar um ID inexistente")
    void naoDeveFalharAoAtualizarIdInexistente() {
        Livro livroInexistente = new Livro(999, "Livro Fantasma", "Autor Desconhecido", 2000);

        assertDoesNotThrow(() -> {
            livroService.atualizar(livroInexistente);
        });

        assertTrue(livroService.listar().isEmpty(), "Nenhum livro deveria ter sido adicionado à tabela.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    @DisplayName("entradas invalidas: deve falhar ao adicionar livro com título em branco")
    void deveFalharAdicionarTituloEmBranco(String tituloInvalido) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            livroService.adicionar(new Livro(0, tituloInvalido, "Autor Válido", 2023));
        });
        assertEquals("Título é um campo obrigatório.", exception.getMessage());
        assertTrue(livroService.listar().isEmpty(), "Nenhum livro deveria ter sido adicionado.");
    }
}