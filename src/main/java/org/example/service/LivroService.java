package org.example.service;

import org.example.model.Livro;

import java.util.*;

public class LivroService {
    private final Map<Integer, Livro> repositorio = new HashMap<>();
    private int ultimoId = 0;

    public int gerarId() {
        return ++ultimoId;
    }

    public void adicionar(Livro livro) {
        repositorio.put(livro.getId(), livro);
    }

    public Livro buscar(int id) {
        Livro livro = repositorio.get(id);
        if (livro == null) {
            throw new NoSuchElementException("Livro não encontrado");
        }
        return livro;
    }

    public void atualizar(Livro livro) {
        int id = livro.getId();
        if (!repositorio.containsKey(id)) {
            throw new NoSuchElementException("Livro não encontrado");
        }
        repositorio.put(livro.getId(), livro);
    }

    public void remover(int id) {
        if (!repositorio.containsKey(id)) {
            throw new NoSuchElementException("Livro não encontrado");
        }
        repositorio.remove(id);
    }

    public Collection<Livro> listar() {
        return repositorio.values();
    }
}
