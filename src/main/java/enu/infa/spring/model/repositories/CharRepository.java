package enu.infa.spring.model.repositories;


import enu.infa.spring.model.BattleUser;
import enu.infa.spring.model.WowChar;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CharRepository extends CrudRepository<WowChar, Integer> {

    List<WowChar> findAllByName(String name);

    List<WowChar> findAllByRealm(String realm);

    List<WowChar> findAllByRegion(String region);

    Optional<WowChar> findByRegionAndRealmAndName(String region, String realm, String name);

    List<WowChar> findAllByOwner(BattleUser user);

}
