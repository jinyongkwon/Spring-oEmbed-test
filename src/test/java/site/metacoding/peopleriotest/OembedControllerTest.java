package site.metacoding.peopleriotest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class OembedControllerTest {

    // oEmbed 연결
    @Test
    public void connect() throws IOException {
        StringBuilder sb = new StringBuilder();
        String host = "https://www.youtube.com/oembed?format=json&url=https://www.youtube.com/watch?v=5qap5aO4i9A";
        URL url = new URL(host);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        while (true) {
            String input = br.readLine();

            if (input == null)
                break;
            sb.append(input);
        }
        System.out.println(sb.toString());
    }
}
