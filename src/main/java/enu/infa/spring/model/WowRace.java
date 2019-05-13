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
public class WowRace {

    @Id
    private Integer id;
    private Integer mask;
    private String side;
    private String name;
}




