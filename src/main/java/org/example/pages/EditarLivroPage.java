package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public class EditarLivroPage {
    private final WebDriver driver;

    public EditarLivroPage(WebDriver driver) {
        this.driver = driver;
        if (!Objects.equals(driver.getTitle(), "Editar Livro")) {
            throw new IllegalStateException("Esta não é a página de editar livro.");
        }
    }

    public void atualizarAno(String novoAno) {
        WebElement anoInput = driver.findElement(By.id("anoPublicacao"));
        anoInput.clear();
        anoInput.sendKeys(novoAno);
    }

    public void submeterFormulario() {
        driver.findElement(By.className("btn-success")).click();
    }
}