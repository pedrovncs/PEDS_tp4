package org.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {
    @Test
    @DisplayName("deve falhar ao criar livro com título vazio")
    void deveFalharAoCriarLivroComTituloVazio() {
        String tituloVazio = "";
        String autor = "Kurt Vonnegut";
        int ano = 1973;
        int id = 1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Livro(id, tituloVazio, autor, ano);
        });

        assertEquals("Título é um campo obrigatório.", exception.getMessage());
    }

    @Test
    @DisplayName("deve falhar ao criar livro com autor nulo")
    void deveFalharAoCriarLivroComAutorNulo() {
        String titulo = "Breakfast of Champions";
        String autorNulo = null;
        int ano = 1973;
        int id = 2;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Livro(id, titulo, autorNulo, ano);
        });

        assertEquals("Autor é um campo obrigatório.", exception.getMessage());
    }

}