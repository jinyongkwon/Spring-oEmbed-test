package site.metacoding.peopleriotest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import site.metacoding.peopleriotest.handler.exception.CustomApiException;

@Service
public class OembedServiceImpl implements OembedService {

    // oEmbed에 requst해서 데이터를 받는 메서드
    public String oembedConnect(String url) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder();
        String host = oembedHost(url);
        URL url3 = new URL(host);
        HttpURLConnection conn = (HttpURLConnection) url3.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        while (true) {
            String input = br.readLine();
            if (input == null)
                break;
            sb.append(input);
        }
        return sb.toString();
    }

    public String oembedHost(String url) throws FileNotFoundException, IOException, ParseException {
        String host = null;
        if (providerCheckUrl(url)) {
            if (providerUrl(url).contains("vimeo")) {
                host = providerUrl(url) + "?url=" + url;
            } else if (providerUrl(url).contains("instagram")) {
                host = providerUrl(url) + "?url=" + url + "&access_token=26e360f57bc139247c4e1158a5a80691";
            } else {
                host = providerUrl(url) + "?format=json&url=" + url;
            }
        } else {
            throw new CustomApiException("지원하지 않는 주소입니다.");
        }
        return host;
    }

    // 입력받은 Url에서 Host만 추출하는 메서드
    public String urlGetHost(String requstUrl) throws MalformedURLException {
        URL url = new URL(requstUrl);
        String host = url.getHost();
        String[] hostParsing = host.split("\\."); // .만 쓰면 단어가 토큰화되므로 \를 붙여야하는데 \를 사용하기 위해서 \\를 써야하므로 \\.을 사용
        if (hostParsing.length == 2) {
            return hostParsing[0];
        }
        if (hostParsing.length == 3) {
            return hostParsing[1];
        }
        throw new CustomApiException("유효하지 않은 주소입니다.");
    }

    // 입력받은 url의 사이트명을 가지고 해당 provider의 endpoints를 찾음.
    public JSONObject providerCheckEndpoints(String requstUrl)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject endpoints = null;
        String domainName = urlGetHost(requstUrl);
        JSONArray providerList = (JSONArray) new JSONParser()
                .parse(new FileReader("src/main/resources/static/provider/provider.json"));
        for (int i = 0; i < providerList.size(); i++) {
            JSONObject provider = (JSONObject) providerList.get(i);
            String providerUrl = provider.get("provider_url").toString();
            String providerDomainName = urlGetHost(providerUrl);
            if (domainName.equalsIgnoreCase(providerDomainName)) {
                JSONArray endpointList = (JSONArray) provider.get("endpoints");
                endpoints = (JSONObject) endpointList.get(0);
            }
        }
        return endpoints;
    }

    // provider에 있는 Url를 찾음.
    public String providerUrl(String requstUrl) throws FileNotFoundException, IOException, ParseException {
        String url = providerCheckEndpoints(requstUrl).get("url").toString();
        if (url.contains("{format}")) {
            url = url.replace("{format}", "json");
        }
        return url;
    }

    // provider의 schemes와 입력받은 url을 비교해서 사용가능한지 확인하는 메서드
    public boolean providerCheckUrl(String url) throws FileNotFoundException, IOException, ParseException {
        String checkUrl = url;
        JSONArray schemesList = (JSONArray) providerCheckEndpoints(url).get("schemes");
        boolean possible = false;

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
            if (checkUrl.matches(schemes.toString())) { // matches로 정규표현식으로 비교
                possible = true;
            }
        }
        return possible;
    }
}
