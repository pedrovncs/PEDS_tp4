package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public class ListaPage {
    private final WebDriver driver;

    public ListaPage(WebDriver driver) {
        this.driver = driver;

        if (!Objects.equals(driver.getTitle(), "Meus Livros")) {
            throw new IllegalStateException("Esta não é a página de listagem de livros. Título atual: " + driver.getTitle());
        }
    }

    public void clicarBotaoAdicionar() {
        driver.findElement(By.linkText("Adicionar Livro")).click();
    }

    public void clicarBotaoEditarPrimeiroLivro() {
        driver.findElement(By.cssSelector(".card .btn-outline-secondary")).click();
    }

    public void clicarBotaoDeletarPrimeiroLivro() {
        driver.findElement(By.cssSelector(".card .btn-outline-danger")).click();
        driver.switchTo().alert().accept();
    }

    public String getTituloPrimeiroLivro() {
        return driver.findElement(By.cssSelector(".card-title")).getText();
    }

    public String getAnoPrimeiroLivro() {
        WebElement smallElement = driver.findElement(By.cssSelector(".card-text small"));
        return smallElement.getText();
    }

    public boolean contemLivroComTitulo(String titulo) {
        return driver.findElements(By.xpath("//h5[text()='" + titulo + "']")).size() > 0;
    }

    public String getMensagemNenhumLivro() {
        return driver.findElement(By.className("alert-info")).getText();
    }
}