package tzamsk.Soteria.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BungieAPIController {

    @Value("${bungie.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private String rootURL = "https://www.bungie.net/Platform/Destiny2";

    @GetMapping("/profile/{membershipType}/{membershipId}")
    @ResponseBody
    public String getUserProfile(@PathVariable String membershipType,
                                 @PathVariable String membershipId,
                                 @RequestParam(defaultValue = "0") int components){

        String url = String.format("%s/%s/Profile/%s/?components=%d", rootURL, membershipType, membershipId, components);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        return response;
    }
}
