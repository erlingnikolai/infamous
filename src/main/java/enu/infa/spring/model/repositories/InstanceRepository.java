package enu.infa.spring.model.repositories;


import enu.infa.spring.model.BattleUser;
import enu.infa.spring.model.WowChar;
import enu.infa.spring.model.WowInstance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InstanceRepository extends CrudRepository<WowInstance, Integer> {


}
