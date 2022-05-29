package site.metacoding.peopleriotest;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class ParsingTest {

    @Test
    public void urlparsing() throws MalformedURLException {
        URL url = new URL("https://vimeo.com/20097015");
        String host = url.getHost();
        String[] hostParsing = host.split("\\."); // .만 쓰면 단어가 토큰화되므로 \를 붙여야하는데 \를 사용하기 위해서 \\를 써야하므로 \\.을 사용
        if (hostParsing.length == 2) {
            System.out.println(hostParsing[0]);
        }
        if (hostParsing.length == 3) {
            System.out.println(hostParsing[1]);
        }
    }

}
