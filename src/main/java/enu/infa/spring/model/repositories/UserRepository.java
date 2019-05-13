package enu.infa.spring.model.repositories;


import enu.infa.spring.model.BattleUser;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<BattleUser, Integer> {


}
