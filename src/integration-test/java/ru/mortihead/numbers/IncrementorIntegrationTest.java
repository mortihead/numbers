package ru.mortihead.numbers;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.*;

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
        System.out.println("Session ID: " + jSession);

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


    /**
     * Метод для тестирования одновременных вызовов счетчика по Rest-сервису
     *
     * @param count количество вызовов
     * @return getnumber, который возвращается в конце
     */
    private int getNumber_after_call_incrementnumber(int count) {
        System.out.println(String.format("getNumber_after_call_incrementnumber, count: %s", count));
        int result = -1;
        try {
            // Первый вызов, чтобы получить jsession
            String url = "http://localhost:8082/service/getnumber";
            URL obj = new URL(url);
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

            int g = 0;
            String jSession = "";
            while (http.getHeaderField(g) != null) {
                // System.out.println(http.getHeaderField(g));
                if (http.getHeaderField(g).startsWith("JSESSION")) {
                    jSession = http.getHeaderField(g);
                }
                g++;
            }

            // Теперь делаем incrementnumber в той же сессии

            for (int i = 0; i < count; i++) {

                url = "http://localhost:8082/service/incrementnumber";
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

                result = Integer.valueOf(response.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        System.out.println(String.format("getNumber_after_call_incrementnumber, result: %s", result));
        return result;
    }

    class ConcurrencyResult {
        int expectedValue;
        int resultingValue;

        ConcurrencyResult(int expectedValue, int resultingValue) {
            this.expectedValue = expectedValue;
            this.resultingValue = resultingValue;
        }

        @Override
        public String toString() {
            return String.format("expectedValue: %d, resultingValue: %d", expectedValue, resultingValue);
        }
    }

    @Test
    public void testConcurrency() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        System.out.println("--- testConcurrency ---");
        // количество потоков
        int threads = 100;
        // сильно нагружать не буду, 10 обращений достаточно
        final int max_call_count = 10; // Количество вызовов рандомно, ограничено этим значением

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        Collection<Future<ConcurrencyResult>> futures = new ArrayList<>(threads);
        for (int t = 0; t < threads; ++t) {
            futures.add(executorService.submit(new Callable<ConcurrencyResult>() {
                @Override
                public ConcurrencyResult call() {
                    int count = 1 + (int) (Math.random() * max_call_count);
                    return new ConcurrencyResult(count, getNumber_after_call_incrementnumber(count));
                }
            }));
        }

        for (Future<ConcurrencyResult> f : futures) {
            ConcurrencyResult result = f.get();
            System.out.println("Get future result : " + result);
            assertEquals(result.expectedValue, result.resultingValue);
        }
        System.out.println("--- testConcurrency finished ---");
    }
}
