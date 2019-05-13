package enu.infa.spring.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WowMount {


    String name;

    @Id
    int spellId;
    int creatureId;
    int itemId;
    int qualityId;
    String icon;
    boolean isFlying;
    boolean isAquatic;
    boolean isJumping;
    boolean isGround;


}
