package enu.infa.spring.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import enu.infa.spring.ApiRequest;
import lombok.*;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor // <--- THIS is it
public class WowChar {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    String name;
    String realm;
    String battlegroup;
    String region;
    String currentInstance;


    double neckLevel;
    int averageItemLevel;
    int averageItemLevelEquipped;

    int currentKeyLevel;
    int mythicPlus;
    int raceId;
    int level;
    String thumbnail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wowClass")
    WowClass wowClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    BattleUser owner;

    public String getItemLevel() {
        return (averageItemLevel == averageItemLevelEquipped) ? String.valueOf(averageItemLevel) : averageItemLevelEquipped + "/" + averageItemLevel;
    }

    public String getPictureUrl() {
        return "https://render-" + region + ".worldofwarcraft.com/character/" + thumbnail;
    }

    public String getEncodedName() {
        return encode(name).toLowerCase();
    }

    public String getEncodedRealm() {
        return encode(realm.replace(" ", "-").toLowerCase());
    }

    private String encode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }


    private JsonElement parseJson(JsonObject json, String... values) {
        JsonObject parsedJson = json;
        for (int i = 0; i < values.length - 1; i++) {
            parsedJson = parsedJson.get(values[i]).getAsJsonObject();
        }
        return parsedJson.get(values[values.length - 1]);
    }


    /**
     * Goes through a character and checks the blizzard api for new values.
     */
    public void update() {
        JsonObject charRequest = ApiRequest.getCharFields(this, ApiRequest.CharField.values());
       // JsonObject mythicPlus = ApiRequest.sendMythicPlusRequest(this);
        JsonObject azerite = parseJson(charRequest, "items", "neck", "azeriteItem").getAsJsonObject();
        float remaining = (float) azerite.get("azeriteExperience").getAsInt() / azerite.get("azeriteExperienceRemaining").getAsInt();
        this.setNeckLevel(azerite.get("azeriteLevel").getAsInt() + Math.round(remaining * 100.0) / 100.0);
        this.setAverageItemLevel(parseJson(charRequest, "items", "averageItemLevel").getAsInt());
        this.setAverageItemLevelEquipped(parseJson(charRequest, "items", "averageItemLevelEquipped").getAsInt());
    //    setMythicPlus(mythicPlus != null ? getHighestKey(mythicPlus) : 0);
    }


    /**
     * currently not active
     * Need a better concept to track what key is ran each week.
     * With the knowledge learned in the other project this should be doable.
     * @param mPlusList
     * @return
     */
    private int getHighestKey(JsonObject mPlusList) {
        Date start = new Date(1551855600000L); //start;
        Date end = new Date(start.getTime() + 604800000L);
        int highestKey = 0;
        if (mPlusList != null) {
            for (JsonElement mRun : mPlusList.get("best_runs").getAsJsonArray()) {
                Date mDate = new Date(mRun.getAsJsonObject().get("completed_timestamp").getAsLong());
                if (mDate.after(start) && mDate.before(end) && mRun.getAsJsonObject().get("keystone_level").getAsInt() > highestKey) {
                    highestKey = mRun.getAsJsonObject().get("keystone_level").getAsInt();
                }
            }
        }
        return highestKey;
    }
}