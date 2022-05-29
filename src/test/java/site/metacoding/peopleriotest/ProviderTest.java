package site.metacoding.peopleriotest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ProviderTest {

    // providerEndPoint찾기.
    public JSONObject providerEndPoint() throws FileNotFoundException, IOException, ParseException {
        JSONArray provider = (JSONArray) new JSONParser()
                .parse(new FileReader("src/test/java/site/metacoding/peopleriotest/TestProvider.json"));
        JSONObject youtube = (JSONObject) provider.get(0);
        JSONArray endpointList = (JSONArray) youtube.get("endpoints");
        return (JSONObject) endpointList.get(0);
    }

    // provider에서 url에 해당하는 인덱스 찾기.
    @Test
    public void providerUrlAndUrlCheck() throws FileNotFoundException, IOException, ParseException {
        URL url = new URL("https://www.instagram.com/p/BUawPlPF_Rx/");
        String urlHost = url.getHost();
        String domainName = "";
        String[] hostParsing = urlHost.split("\\.");
        if (hostParsing.length == 2) {
            domainName = hostParsing[0];
        }
        if (hostParsing.length == 3) {
            domainName = hostParsing[1];
        }
        System.out.println(domainName);
        JSONArray providerList = (JSONArray) new JSONParser()
                .parse(new FileReader("src/test/java/site/metacoding/peopleriotest/TestProvider.json"));
        for (int i = 0; i < providerList.size(); i++) {
            JSONObject provider = (JSONObject) providerList.get(i);
            URL providerUrl = new URL(provider.get("provider_url").toString());
            String providerUrlHost = providerUrl.getHost();
            String providerDomainName = "";
            String[] providerHostParsing = providerUrlHost.split("\\.");
            if (providerHostParsing.length == 2) {
                providerDomainName = providerHostParsing[0];
            }
            if (providerHostParsing.length == 3) {
                providerDomainName = providerHostParsing[1];
            }
            System.out.println(providerDomainName);
        }

    }

    // provider의 url 확인.
    @Test
    public void providerUrl() throws FileNotFoundException, IOException, ParseException {
        String url = providerEndPoint()
                .get("url")
                .toString();
        System.out.println(url);
    }

    // Oembed 지원하는 Url인지 확인.
    @Test
    public void providerCheckUrl() throws FileNotFoundException, IOException, ParseException {
        String url = "https://www.youtube.com/watch?v=dBD54EZIrZo";
        JSONArray schemesList = (JSONArray) providerEndPoint().get("schemes");

        for (int i = 0; i < schemesList.size(); i++) { // 정규표현식을 사용하기위해 schemes에 있는 url의 *앞에 .을 붙여주기 위한 반복문
            StringBuilder schemes = new StringBuilder(schemesList.get(i).toString());
            // StringBuilder의 insert메서드를 사용하기 위해 StringBuilder에 담음.

            int index = schemesList
                    .get(i)
                    .toString()
                    .indexOf("*"); // 최초의 * 위치 확인
            schemes.insert(index, "."); // * 위치 앞에 .을 삽입
            while (index != -1) { // 다음위치에 *을 찾기위해 반복, indexof는 못찾을경우 -1을 반환.
                index = schemesList
                        .get(i)
                        .toString()
                        .indexOf("*", index + 1);
                // * 을 찾는데 시작위치를 index위치에서 찾을경우 무한 반복하기 때문에 +1(찾는 문자열의 크기)을 해줌
                if (index != -1) { // index가 -1이 아닐경우에만 insert
                    schemes.insert(index + 1, ".");
                }
            }
            System.out.println(schemes);
            System.out.println(url.matches(schemes.toString()));
        }
    }

}
