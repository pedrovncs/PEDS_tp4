package org.example.service;

import org.example.model.Livro;
import org.example.repository.LivroRepository;

import java.util.Collection;
import java.util.Optional;

public class LivroService {

    private final LivroRepository repository;

    public LivroService() {
        this.repository = new LivroRepository();
    }

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public void adicionar(Livro livro) {
        repository.save(livro);
    }

    public Collection<Livro> listar() {
        return repository.findAll();
    }

    public Optional<Livro> buscarPorId(int id) {
        return repository.findById(id);
    }

    public void atualizar(Livro livro) {
        repository.update(livro);
    }

    public void remover(int id) {
        repository.delete(id);
    }

    public void limparBanco() {
        repository.deleteAll();
    }
}