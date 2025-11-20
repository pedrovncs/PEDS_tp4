package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class AddLivroPage {
    private final WebDriver driver;

    public AddLivroPage(WebDriver driver) {
        this.driver = driver;
        if (!Objects.equals(driver.getTitle(), "Adicionar Livro")) {
            throw new IllegalStateException("Esta não é a página de adicionar livro.");
        }
    }

    public void preencherFormulario(String titulo, String autor, String ano) {
        driver.findElement(By.id("titulo")).sendKeys(titulo);
        driver.findElement(By.id("autor")).sendKeys(autor);
        driver.findElement(By.id("anoPublicacao")).sendKeys(ano);
    }

    public void submeterFormulario() {
        driver.findElement(By.className("btn-success")).click();
    }
}