package tzamsk.Soteria.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class BungieAPIController {

    @Value("${bungie.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private String rootURL = "https://www.bungie.net/Platform/Destiny2";

    @GetMapping("/profile/{membershipType}/{membershipId}")
    @ResponseBody
    public ModelAndView getUserProfile(@PathVariable String membershipType,
                                 @PathVariable String membershipId){

        String url = String.format("%s/%s/Profile/%s/?components=%d", rootURL, membershipType, membershipId, 100);
        String charactersUrl = String.format("%s/%s/Profile/%s/?components=%d", rootURL, membershipType, membershipId, 200);
        String triumphsUrl = String.format("%s/%s/Profile/%s/?components=%d", rootURL, membershipType, membershipId, 900);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> charactersResponse = restTemplate.exchange(charactersUrl, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> triumphsResponse = restTemplate.exchange(triumphsUrl, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();

        String displayName = "";
        int currentGuardianRank = 0;
        int lifetimeHighestGuardianRank = 0;
        int renewedGuardianRank = 0;
        int bungieGlobalDisplayNameCode = 0;

        int currentTriumphScore = 0;
        int legacyTriumphScore = 0;
        int totalTriumphScore = 0;

        List<JsonNode> characters = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode charactersRoot = mapper.readTree(charactersResponse.getBody());
            JsonNode triumphsRoot = mapper.readTree(triumphsResponse.getBody());

            JsonNode charactersData = charactersRoot.path("Response").path("characters").path("data");
            charactersData.forEach(character -> {
                characters.add(character);
            });

            displayName = root.path("Response").path("profile").path("data").path("userInfo").path("displayName").asText();
            bungieGlobalDisplayNameCode = root.path("Response").path("profile").path("data").path("userInfo").path("bungieGlobalDisplayNameCode").asInt();
            currentGuardianRank = root.path("Response").path("profile").path("data").path("currentGuardianRank").asInt();
            lifetimeHighestGuardianRank = root.path("Response").path("profile").path("data").path("lifetimeHighestGuardianRank").asInt();
            renewedGuardianRank = root.path("Response").path("profile").path("data").path("renewedGuardianRank").asInt();

            JsonNode profileRecordsData = triumphsRoot.path("Response").path("profileRecords").path("data");
            currentTriumphScore = profileRecordsData.path("activeScore").asInt();
            legacyTriumphScore = profileRecordsData.path("legacyScore").asInt();
            totalTriumphScore = profileRecordsData.path("lifetimeScore").asInt();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView("profile");

        mav.addObject("profileData", response.getBody());
        mav.addObject("displayName", displayName);
        mav.addObject("bungieGlobalDisplayNameCode", bungieGlobalDisplayNameCode);
        mav.addObject("renewedGuardianRank", renewedGuardianRank);
        mav.addObject("currentGuardianRank", currentGuardianRank);
        mav.addObject("lifetimeHighestGuardianRank", lifetimeHighestGuardianRank);
        mav.addObject("renewedGuardianRank", renewedGuardianRank);
        mav.addObject("characters", characters);

        mav.addObject("currentTriumphScore", currentTriumphScore);
        mav.addObject("legacyTriumphScore", legacyTriumphScore);
        mav.addObject("totalTriumphScore", totalTriumphScore);
        return mav;
    }

    @GetMapping("/profile/{membershipType}/{membershipId}/{characterId}")
    @ResponseBody
    public ModelAndView getUserCharacterProfile(@PathVariable String membershipType,
                                                @PathVariable String membershipId,
                                                @PathVariable String characterId){

        String characterUrl = String.format("%s/%s/Profile/%s/Character/%s/?components=%d", rootURL, membershipType, membershipId, characterId, 900);
        String emblemUrl = String.format("%s/%s/Profile/%s/Character/%s/?components=%d", rootURL, membershipType, membershipId, characterId, 200);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> characterResponse = restTemplate.exchange(characterUrl, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> emblemResponse = restTemplate.exchange(emblemUrl, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();

        String emblemBackgroundPath = "";
        try {
            JsonNode characterData = mapper.readTree(emblemResponse.getBody()).path("Response").path("characters").path("data").path(characterId);
            JsonNode emblemNode = characterData.path("emblemHash");

            if (!emblemNode.isMissingNode()) {
                String emblemHash = emblemNode.asText();
                String emblemDetailsUrl = String.format("https://www.bungie.net/Platform/Destiny2/Manifest/DestinyInventoryItemDefinition/%s/", emblemHash);

                ResponseEntity<String> emblemDetailsResponse = restTemplate.exchange(emblemDetailsUrl, HttpMethod.GET, entity, String.class);
                JsonNode emblemDetails = mapper.readTree(emblemDetailsResponse.getBody()).path("Response");

                emblemBackgroundPath = emblemDetails.path("secondarySpecial").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView("profile-character");
        mav.addObject("characterStats", characterResponse.getBody());
        mav.addObject("emblemBackgroundPath", emblemBackgroundPath);
        return mav;
    }
}
