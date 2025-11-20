package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class LivroHttpIntegrationTest {

    @Test
    @DisplayName("conexao bem sucedida")
    void testValidConnection() throws IOException {
        URL url = URI.create("http://localhost:7070" + "/").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();

        assertEquals(200, responseCode, "conexao deveria ser bem sucedida na URL informada");
    }

    @Test
    @DisplayName("falha de rede: conexao recusada porta invallida")
    void testFalhaConexaoPortaErrada() {
        assertThrows(Exception.class, () -> {
            URL url = URI.create("http://localhost:9999").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
        }, "deve lancar Exception");
    }

    @Test
    @DisplayName("falha de rede: timeout")
    void testTimeoutUrlErrado() {
        assertThrows(Exception.class, () -> {
            URL url = URI.create("http:????" + "/").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(500);
            conn.connect();
        }, "deve lancar Exception");
    }

}