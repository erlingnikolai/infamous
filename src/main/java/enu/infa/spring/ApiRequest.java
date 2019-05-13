package enu.infa.spring;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import enu.infa.spring.model.WowChar;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;

import java.util.*;

public class ApiRequest {


    private static String locale = "locale=en_GB";

    private static String https = "https://";
    private static String api = ".api.blizzard.com";
    private static String preToken = "&access_token=";

    private static JsonParser jsonParser = new JsonParser();


    public static JsonObject sendApiRequest(Region region, Request request, String token) {
        return getResponse(https + region + api + request.value + locale + preToken + token);
    }

    public static JsonObject sendApiAccountRequest(String region, String token) {
        return getResponse(https + region + api + "/wow/user/characters?access_token=" + token);
    }


    public enum Region {
        US, EU, KR, TW
    }

    public enum CharField {
        // @formatter:off
        ACHIEVEMENTS,
        APPEARANCE,
        FEED,
        GUILD,
        HUNTERPETS,
        ITEMS,
        MOUNTS,
        PETS,
        PETSLOTS,
        PROFESSIONS,
        PROGRESSION,
        PVP,
        QUESTS,
        REPUTATION,
        STATISTICS,
        STATS,
        TALENTS,
        TITLES,
        AUDIT;
        // @formatter:on


    }

    public enum Request {
        // @formatter:off
        MOUNT(  "/wow/mount/?"),
    ACHIEVEMENT("/wow/data/character/achievements?"),
        RACES(  "/wow/data/character/races?"),
        CLASSES("/wow/data/character/classes?"),
        REALM(  "/data/wow/realm/index?namespace=dynamic-eu&");

        // @formatter:on
        private final String value;

        Request(String value) {
            this.value = value;
        }
    }


    static JsonObject getResponse(String request) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(request).openConnection();
            if (con.getResponseCode() != 404 || con.getResponseCode() != 401) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer testing = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    testing.append(inputLine);
                }
                in.close();
                return (JsonObject) jsonParser.parse(String.valueOf(testing));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Type getTypeToken(Class type) {
        return TypeToken.getParameterized(ArrayList.class, type).getType();
    }


    public static JsonObject sendMythicPlusRequest(WowChar wowC) {
        String startUrl = https + wowC.getRegion() + api + "/profile/wow/character/";
        String middleUrl = "/mythic-keystone-profile/season/2?namespace=profile-eu&locale=en_EU";
        return getResponse(startUrl + wowC.getEncodedRealm() + "/" + wowC.getEncodedName() + middleUrl + preToken + wowC.getOwner().getAccessToken());
    }

    public static JsonObject getCharFields(WowChar wowC, CharField... charFields) {
        String startUrl = https + wowC.getRegion() + api + "/wow/character/";
        String fields = "?fields=" + Arrays.toString(charFields).toLowerCase().replaceAll("[\\[\\]]", "").replace(", ", "%2C").replace("petslots", "petsSlots").replace("hunterpets", "hunterPets");

        return getResponse(startUrl + wowC.getEncodedRealm() + "/" + wowC.getEncodedName() + fields + "&locale=en_GB" + preToken + wowC.getOwner().getAccessToken());
    }


    public static void main(String[] args) {

    }
}
