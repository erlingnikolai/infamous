package enu.infa.spring;

import enu.infa.spring.model.WowChar;
import enu.infa.spring.model.repositories.CharRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class ScheduledTasks {

    private final CharRepository charRepository;


    public ScheduledTasks(CharRepository wowCharRepository) {
        this.charRepository = wowCharRepository;
    }

    /**
     * We want to refresh all characters at set intervals.
     * Needs to be adapted to not go over blizzards api limit
     */
    @Scheduled(fixedRate = 60000)
    private void test() {
        Iterable<WowChar> charList = charRepository.findAll();
        charList.forEach(WowChar::update);
        charRepository.saveAll(charList);
    }
}