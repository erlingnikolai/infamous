package enu.infa.spring.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BattleUser {

    @Id
    private Integer id;


    private String battleTag;

    private String accessToken;

    @OneToMany(mappedBy = "owner")
    private List<WowChar> characters;

    public BattleUser(String newId, String battletag, String accessToken) {
        this.id = Integer.parseInt(newId);
        this.battleTag = battletag;
        this.accessToken = accessToken;
    }

    public double getAverageItemLevel() {
        return Math.round(characters.stream().mapToInt(p -> p.averageItemLevel).average().orElse(0) * 100.0) / 100.0;
    }

    public double getAverageNeckLevel() {
        return Math.round(characters.stream().mapToDouble(p -> p.neckLevel).average().orElse(0) * 100.0) / 100.0;
    }

    public List<WowChar> getCharactersSorted() {
        getCharacters().sort(Comparator.comparing(WowChar::getNeckLevel).reversed());
        return characters;
    }



}




