package site.metacoding.peopleriotest.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.parser.ParseException;

public interface OembedService {

    public String oembedConnect(String Url) throws MalformedURLException, IOException, ParseException;

}
