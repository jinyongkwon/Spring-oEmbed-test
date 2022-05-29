package site.metacoding.peopleriotest.web;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.peopleriotest.service.OembedService;

@RequiredArgsConstructor
@Controller
public class OembedController {

    private final OembedService oembedService;

    @GetMapping("/oembed")
    public ResponseEntity<?> oembed(String url) throws MalformedURLException, IOException, ParseException {
        String data = oembedService.oembedConnect(url);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
