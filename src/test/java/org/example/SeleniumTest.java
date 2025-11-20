package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.config.Database;
import org.example.pages.AddLivroPage;
import org.example.pages.EditarLivroPage;
import org.example.pages.ListaPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LivroWebAppTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String APP_URL = "http://localhost:7070";

    @BeforeAll
    void setupAll() {
        WebDriverManager.chromedriver().setup();
        Database.initialize();
    }

    @BeforeEach
    void setup() {
        limparTabelaLivros();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Deve exibir a mensagem 'Nenhum livro encontrado' quando a lista está vazia")
    void deveListarTodos() {
        driver.get(APP_URL);
        ListaPage listPage = new ListaPage(driver);
        assertEquals("Nenhum livro encontrado!", listPage.getMensagemNenhumLivro());
    }

    @ParameterizedTest
    @CsvSource({
            "O Senhor dos Anéis, J.R.R. Tolkien, 1954",
            "1984, George Orwell, 1949",
            "Dom Quixote, Miguel de Cervantes, 1605"
    })
    @DisplayName("Deve adicionar múltiplos livros via teste parametrizado")
    void deveAdicionarMultiplosLivros(String titulo, String autor, String ano) {
        driver.get(APP_URL);

        wait.until(ExpectedConditions.titleContains("Meus Livros"));
        ListaPage listPage = new ListaPage(driver);
        listPage.clicarBotaoAdicionar();

        AddLivroPage addBookPage = new AddLivroPage(driver);
        wait.until(ExpectedConditions.titleContains("Adicionar Livro"));
        addBookPage.preencherFormulario(titulo, autor, ano);
        addBookPage.submeterFormulario();

        wait.until(ExpectedConditions.titleContains("Meus Livros"));

        listPage = new ListaPage(driver);
        assertTrue(listPage.contemLivroComTitulo(titulo), "O livro '" + titulo + "' não foi encontrado na lista.");
    }


    @Test
    @DisplayName("Deve adicionar, editar e depois remover um livro")
    void deveAdicionarEditarERemoverUmLivro() {
        driver.get(APP_URL);
        ListaPage listPage = new ListaPage(driver);
        listPage.clicarBotaoAdicionar();
        wait.until(ExpectedConditions.titleContains("Adicionar Livro"));

        AddLivroPage addBookPage = new AddLivroPage(driver);
        addBookPage.preencherFormulario("Código Limpo", "Robert C. Martin", "2008");
        addBookPage.submeterFormulario();
        wait.until(ExpectedConditions.titleContains("Meus Livros"));

        listPage = new ListaPage(driver);
        assertEquals("Código Limpo", listPage.getTituloPrimeiroLivro());
        assertTrue(listPage.getAnoPrimeiroLivro().contains("2008"));

        listPage.clicarBotaoEditarPrimeiroLivro();
        wait.until(ExpectedConditions.titleIs("Editar Livro"));
        EditarLivroPage editPage = new EditarLivroPage(driver);
        editPage.atualizarAno("2009");
        editPage.submeterFormulario();
        wait.until(ExpectedConditions.titleIs("Meus Livros"));

        listPage = new ListaPage(driver);
        assertEquals("Código Limpo", listPage.getTituloPrimeiroLivro());
        assertTrue(listPage.getAnoPrimeiroLivro().contains("2009"), "O ano do livro não foi atualizado.");

        listPage.clicarBotaoDeletarPrimeiroLivro();

        listPage = new ListaPage(driver);
        assertEquals("Nenhum livro encontrado!", listPage.getMensagemNenhumLivro());
    }

    private void limparTabelaLivros() {
        String sql = "DELETE FROM livros;";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Deve falhar com formulario vazio ( teste entrada invalida")
    void deveFalharAdicionarFormularioVazio() {
        driver.get(APP_URL);
        ListaPage listPage = new ListaPage(driver);
        listPage.clicarBotaoAdicionar();
        wait.until(ExpectedConditions.titleContains("Adicionar Livro"));

        AddLivroPage addPage = new AddLivroPage(driver);
        addPage.submeterFormulario();

        assertEquals("Adicionar Livro", driver.getTitle(), "Deveria permanecer na página de adicionar devido à validação HTML.");

    }

    @Test
    @DisplayName("simular timeout com elemento inexistente na pagina")
    void deveLancarTimeoutAoEsperarElementoInexistente() {
        driver.get(APP_URL);

        WebDriverWait waitCurto = new WebDriverWait(driver, Duration.ofSeconds(3));

        TimeoutException exception = assertThrows(TimeoutException.class, () -> {
            waitCurto.until(ExpectedConditions.presenceOfElementLocated(By.id("elemento-que-nao-existe")));
        }, "Deveria lançar TimeoutException ao esperar por elemento inexistente.");
    }
}