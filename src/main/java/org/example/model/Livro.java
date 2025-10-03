package org.example.model;

public class Livro {
    private final int id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;

    public Livro(int id,String titulo, String autor, int anoPublicacao) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título é um campo obrigatório.");
        }
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("Autor é um campo obrigatório.");
        }
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getAutor() {
        return autor;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return "Livro{ id: " + id + ", " +
                "titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                '}';
    }
}
