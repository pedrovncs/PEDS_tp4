package org.example.view;

import org.example.model.Livro;
import org.example.service.LivroService;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LivroView {

    enum OpcaoMenu {
        ADICIONAR_LIVRO, LISTAR_LIVROS,  ATUALIZAR_LIVRO, REMOVER_LIVRO, SAIR, INVALIDO,

    }

    private final Scanner scanner = new Scanner(System.in);
    private final LivroService livroService = new LivroService();

    public void iniciar(){
       OpcaoMenu escolha= OpcaoMenu.INVALIDO;
        do {
            System.out.println("\n-- Menu Livros--\n");
            System.out.println("0. Adicionar Livro");
            System.out.println("1. Listar Livros");
            System.out.println("2. Atualizar Livro");
            System.out.println("3. Remover Livro");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int escolhaInt = Integer.parseInt(scanner.nextLine());
                OpcaoMenu[] todasOpcoesEnum = OpcaoMenu.values();
                int opcaoMaxIndex = todasOpcoesEnum.length - 1;
                if (escolhaInt < 0 || escolhaInt > opcaoMaxIndex){
                    escolha= OpcaoMenu.INVALIDO;
                } else {
                    escolha= todasOpcoesEnum[escolhaInt];
                }

            } catch (NumberFormatException e) {
                System.out.println("Opção inválida, insira um valor numérico.");
                escolha= OpcaoMenu.INVALIDO;
                continue;
            }

            switch (escolha) {
                case ADICIONAR_LIVRO -> adicionarLivro();
                case LISTAR_LIVROS -> listarLivros();
                case ATUALIZAR_LIVRO -> atualizarLivro();
                case REMOVER_LIVRO -> removerLivro();
                case SAIR -> System.out.println("Saindo...");
                case INVALIDO -> System.out.println("Opção inválida!");
            }
        } while (escolha != OpcaoMenu.SAIR);
    }

    private void removerLivro() {
        try{
            System.out.print("Digite o ID do livro para remoção: ");
            int id = Integer.parseInt(scanner.nextLine());
            livroService.remover(id);
            System.out.println("Livro removido com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido, insira um valor numérico.");
        } catch (NoSuchElementException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao remover livro: " + e.getMessage());
        }
    }

    private void atualizarLivro() {
        try {
            System.out.print("Digite o ID do livro para atualização: ");
            int id = Integer.parseInt(scanner.nextLine());

            Livro livro = livroService.buscar(id);
            System.out.println("Editando livro: " + livro.getTitulo() + " (ID: " + livro.getId() + ")");

            System.out.print("Digite o novo título do livro (deixe em branco para não alterar): ");
            String titulo = scanner.nextLine();

            System.out.print("Digite o novo autor do livro (deixe em branco para não alterar): ");
            String autor = scanner.nextLine();

            System.out.print("Digite o novo ano do livro (deixe em branco para não alterar): ");
            String anoPublicacao = scanner.nextLine();

            String novoTitulo = (titulo != null && !titulo.trim().isEmpty()) ? titulo : livro.getTitulo();
            String novoAutor = (autor != null && !autor.trim().isEmpty()) ? autor : livro.getAutor();
            int novoAno = (anoPublicacao != null && !anoPublicacao.trim().isEmpty()) ? Integer.parseInt(anoPublicacao) : livro.getAnoPublicacao();

            Livro livroAtualizado = new Livro(id, novoTitulo, novoAutor, novoAno);
            livroService.atualizar(livroAtualizado);
            System.out.println("Livro atualizado com sucesso: ");

        } catch (NumberFormatException e) {
            System.out.println("ID ou ano inválido, insira um valor numérico.");
        } catch (NoSuchElementException e) {
            System.out.println("Erro ao atualizar livro:" + e.getMessage());
        }
    }


    private void listarLivros() {
        Collection<Livro> livros = livroService.listar();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("Repositório de livros:");
            for (Livro livro : livros) {
                System.out.println(livro);
            }
        }
    }

    private void adicionarLivro() {
        try {
            System.out.print("Digite o título do livro: ");
            String titulo = scanner.nextLine();
            System.out.print("Digite o autor do livro: ");
            String autor = scanner.nextLine();
            System.out.print("Digite o ano de publicação do livro: ");
            int ano = Integer.parseInt(scanner.nextLine());

            int novoId = livroService.gerarId();
            Livro novoLivro = new Livro(novoId, titulo, autor, ano);
            livroService.adicionar(novoLivro);
            System.out.println("Livro adicionado com sucesso com o ID: " + novoId);

        } catch (NumberFormatException e) {
            System.out.println("Ano inválido, insira um valor numérico.");
        } catch (NoSuchElementException e) {
            System.out.println("Erro ao atualizar livro:" + e.getMessage()); //captura o erro encontrado pela classe service
        }
    }
}