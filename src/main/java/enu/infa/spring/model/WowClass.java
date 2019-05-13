package enu.infa.spring.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class WowClass {

    @Id
    private Integer id;
    private Integer mask;
    private String powerType;
    private String name;
    private String color;





    public enum Class {
        DEATHKNIGHT("#C41F3B"), DEMONHUNTER("#A330C9"), DRUID("#FF7D0A"), HUNTER("#ABD473"), MAGE("#40C7EB"), MONK("#00FF96"), PALADIN("#F58CBA"), PRIEST("#FFFFFF"), ROGUE("#FFF569"), SHAMAN("#0070DE"), WARLOCK("#8787ED"), WARRIOR("#C79C6E");

        private final String colorValue;

        Class(String value) {
            this.colorValue = value;
        }

        public static String getValue(String name) {
            if (name.equals("Death Knight")) {
                return Class.DEATHKNIGHT.colorValue;
            } else if (name.equals("Demon Hunter")) {
                return Class.DEMONHUNTER.colorValue;
            } else {
                return Class.valueOf(name.toUpperCase()).colorValue;
            }
        }
    }


}




