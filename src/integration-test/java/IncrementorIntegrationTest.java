package ru.mortihead.numbers;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class IncrementorIntegrationTest {

    @Test
    public void whenSendingGet_thenMessageIsReturned() throws IOException {
        String url = "http://localhost:8999/service/getnumber";
        URLConnection connection = new URL(url).openConnection();
        try (InputStream response = connection.getInputStream();
             Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.nextLine();
            assertEquals("0", responseBody);
        }
    }
}
