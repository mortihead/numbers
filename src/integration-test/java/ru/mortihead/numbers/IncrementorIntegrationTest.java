package ru.mortihead.numbers;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class IncrementorIntegrationTest {
    private final String USER_AGENT = "Mozilla/5.0";

    @Test
    public void whenSending_GetNumber_thenZeroIsReturned() throws IOException {
        String url = "http://localhost:8082/service/getnumber";
        System.out.println(String.format("Try to connect and get data from %s", url));
        URLConnection connection = new URL(url).openConnection();
        try (InputStream response = connection.getInputStream();
             Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.nextLine();
            System.out.println("Response: " + responseBody);
            assertEquals("0", responseBody);
        }
    }

    @Test
    public void whenSending_IncrementNumber_then_1_IsReturned() throws IOException {
        String url = "http://localhost:8082/service/incrementnumber";
        System.out.println(String.format("Try to connect and get data from %s", url));
        URLConnection connection = new URL(url).openConnection();
        try (InputStream response = connection.getInputStream();
             Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.nextLine();
            System.out.println("Response: " + responseBody);
            assertEquals("1", responseBody);
        }
    }

    @Test
    public void whenSending_getMaximumValue_then_INT_MAX_VALUE_IsReturned() throws IOException {
        String url = "http://localhost:8082/service/getmaximumvalue";
        System.out.println(String.format("Try to connect and get data from %s", url));
        URLConnection connection = new URL(url).openConnection();
        try (InputStream response = connection.getInputStream();
             Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.nextLine();
            System.out.println("Response: " + responseBody);
            assertEquals(String.valueOf(Integer.MAX_VALUE), responseBody);
        }
    }


    @Test
    public void whenSending_setMaximumValue_105_then_105_IsReturned() throws IOException {
        String url = "http://localhost:8082/service/setmaximumvalue?value=105";
        URL obj = new URL(url);
        System.out.println(String.format("Try to connect and get data from %s", url));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        int g = 0;
        String jSession = "";
        while (http.getHeaderField(g) != null) {
            // System.out.println(http.getHeaderField(g));
            if (http.getHeaderField(g).startsWith("JSESSION")) {
                jSession = http.getHeaderField(g);
            }
            g++;
        }
        System.out.println("Session ID: "+jSession);

        // Теперь проверим чему стало равно maximumvalue в той же сессии

        url = "http://localhost:8082/service/getmaximumvalue";
        System.out.println(String.format("Try to connect and get data from %s from %s", url, jSession));
        obj = new URL(url);
        http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", USER_AGENT);
        http.setRequestProperty("Cookie", jSession);

        in = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
        response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        assertEquals("105", response.toString());
    }

}
