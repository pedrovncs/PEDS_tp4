package org.example.model;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.NotBlank;
import org.junit.jupiter.api.Assertions;

public class LivroProperties {

    @Property(tries = 1000)
    void construtorDeveFuncionarComDadosValidos(
            @ForAll @NotBlank String titulo,
            @ForAll @NotBlank String autor,
            @ForAll @IntRange(min = -2000, max = 2025) int ano
    ) {
        Livro livro = new Livro(1, titulo, autor, ano);

        Assertions.assertEquals(titulo, livro.getTitulo());
        Assertions.assertEquals(autor, livro.getAutor());
    }

    @Provide
    Arbitrary<String> stringsEmBranco() {
        return Arbitraries.of("", " ", "   ", "", "");
    }

    @Property(tries = 1000)
    void construtorDeveFalharComTituloEmBranco(
            @ForAll("stringsEmBranco") String tituloEmBranco,
            @ForAll @NotBlank String autor,
            @ForAll int ano
    ) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Livro(1, tituloEmBranco, autor, ano);
        });
    }

    @Property(tries = 1000)
    void construtorDeveFalharComAutorEmBranco(
            @ForAll @NotBlank String titulo,
            @ForAll("stringsEmBranco") String autorEmBranco,
            @ForAll int ano
    ) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Livro(1, titulo, autorEmBranco, ano);
        });
    }

    @Provide
    Arbitrary<String> stringsNulas() {
        return Arbitraries.just(null);
    }

    @Property(tries = 1000)
    void construtorDeveFalharComTituloNulo(
            @ForAll("stringsNulas") String tituloNulo,
            @ForAll @NotBlank String autor,
            @ForAll int ano
    ) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Livro(1, tituloNulo, autor, ano);
        });
    }

    @Property(tries = 1000)
    void construtorDeveFalharComAutorNulo(
            @ForAll @NotBlank String titulo,
            @ForAll("stringsNulas") String autorNulo,
            @ForAll int ano
    ) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Livro(1, titulo, autorNulo, ano);
        });
    }

}

