package com.example.NettyCountryInfoProject;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class NettyCountryInfoProjectService {

    @Autowired
    WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;

    public Mono<ObjectNode> gci(ObjectNode input) {
        String country = input.get("countryName").asText();
        String url = "https://restcountries.com/v3.1/name/" + country;

        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
            .map(response -> {
                try {
                    JsonNode jsonData = objectMapper.readTree(response);
                    JsonNode firstCountry = jsonData.get(0);
                    ObjectNode data = objectMapper.createObjectNode();

                    data.put("status", "success");

                    // Essential Information
                    data.put("countryName", firstCountry.path("name").path("common").asText("N/A"));
                    data.put("officialName", firstCountry.path("name").path("official").asText("N/A"));
                    data.put("capital", getFirstArrayElement(firstCountry.path("capital")));
                    data.put("flagPath", firstCountry.path("flags").path("png").asText(""));
                    data.put("flagDescription", firstCountry.path("flags").path("alt").asText(""));
                    data.put("coatOfArms", firstCountry.path("coatOfArms").path("png").asText(""));
                    data.put("cca2", firstCountry.path("cca2").asText("N/A"));
                    data.put("cca3", firstCountry.path("cca3").asText("N/A"));
                    data.put("tld", getFirstArrayElement(firstCountry.path("tld")));

                    // Geographic Information
                    data.put("region", firstCountry.path("region").asText("N/A"));
                    data.put("subregion", firstCountry.path("subregion").asText("N/A"));
                    data.put("area", firstCountry.path("area").asDouble(0));
                    data.put("landlocked", firstCountry.path("landlocked").asBoolean(false));
                    data.put("borders", arrayToString(firstCountry.path("borders"), "None"));
                    data.put("coordinates", formatCoordinates(firstCountry.path("latlng")));
                    data.put("googleMaps", firstCountry.path("maps").path("googleMaps").asText("#"));
                    data.put("openStreetMaps", firstCountry.path("maps").path("openStreetMaps").asText("#"));

                    // People & Culture
                    data.put("population", firstCountry.path("population").asLong(0));
                    data.put("languages", objectValuesToString(firstCountry.path("languages"), "N/A"));
                    data.put("demonyms", firstCountry.path("demonyms").path("eng").path("m").asText("N/A"));

                    // Economic & Administrative
                    data.put("currencies", formatCurrencies(firstCountry.path("currencies")));
                    data.put("callingCode", formatCallingCode(firstCountry.path("idd")));
    

                    // Other Details
                    data.put("timezone", getFirstArrayElement(firstCountry.path("timezones")));
                    data.put("continent", getFirstArrayElement(firstCountry.path("continents")));
                    data.put("drivingSide", firstCountry.path("car").path("side").asText("N/A"));
                    data.put("unMember", firstCountry.path("unMember").asBoolean(false));
                    data.put("independent", firstCountry.path("independent").asBoolean(false));
                    data.put("fifa", firstCountry.path("fifa").asText("N/A"));

                    return data;

                } catch (Exception e) {
                    ObjectNode err = objectMapper.createObjectNode();
                    err.put("status", "failed");
                    err.put("errmsg", e.getMessage());
                    return err;
                }
            })
            .onErrorResume(e -> {
                ObjectNode err = objectMapper.createObjectNode();
                err.put("status", "failed");
                err.put("errmsg", e.getMessage());
                return Mono.just(err);
            });
    }

    // ===== Helper Methods (Industry Standard) =====

    private String arrayToString(JsonNode arrayNode, String defaultValue) {
        if (!arrayNode.isArray() || arrayNode.size() == 0) {
            return defaultValue;
        }
        return StreamSupport.stream(arrayNode.spliterator(), false)
            .map(JsonNode::asText)
            .collect(Collectors.joining(", "));
    }

    private String objectValuesToString(JsonNode objectNode, String defaultValue) {
        if (!objectNode.isObject() || objectNode.size() == 0) {
            return defaultValue;
        }
        return StreamSupport.stream(objectNode.spliterator(), false)
            .map(JsonNode::asText)
            .collect(Collectors.joining(", "));
    }

    private String getFirstArrayElement(JsonNode arrayNode) {
        if (arrayNode.isArray() && arrayNode.size() > 0) {
            return arrayNode.get(0).asText();
        }
        return "N/A";
    }

    private String formatCoordinates(JsonNode latlng) {
        if (latlng.isArray() && latlng.size() >= 2) {
            double lat = latlng.get(0).asDouble();
            double lng = latlng.get(1).asDouble();
            String latDir = lat >= 0 ? "N" : "S";
            String lngDir = lng >= 0 ? "E" : "W";
            return String.format("%.2f°%s, %.2f°%s", 
                Math.abs(lat), latDir, Math.abs(lng), lngDir);
        }
        return "N/A";
    }

    private String formatCurrencies(JsonNode currencies) {
        if (!currencies.isObject() || currencies.size() == 0) {
            return "N/A";
        }
        return StreamSupport.stream(currencies.spliterator(), false)
            .map(currency -> {
                String name = currency.path("name").asText();
                String symbol = currency.path("symbol").asText("");
                return symbol.isEmpty() ? name : name + " (" + symbol + ")";
            })
            .collect(Collectors.joining(", "));
    }

    private String formatCallingCode(JsonNode idd) {
        String root = idd.path("root").asText("");
        JsonNode suffixes = idd.path("suffixes");
        if (!root.isEmpty() && suffixes.isArray() && suffixes.size() > 0) {
            return root + suffixes.get(0).asText();
        }
        return "N/A";
    }
}