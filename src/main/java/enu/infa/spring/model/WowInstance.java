package enu.infa.spring.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor // <--- THIS is it
public class WowInstance {


    String fullName;

    @Id
    @Column(length = 8)
    String shortName;

}
