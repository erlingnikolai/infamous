package enu.infa.spring.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import enu.infa.spring.ApiRequest;
import enu.infa.spring.model.BattleUser;
import enu.infa.spring.model.WowChar;
import enu.infa.spring.model.WowClass;
import enu.infa.spring.model.WowRace;
import enu.infa.spring.model.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.LinkedHashMap;


@Controller
public class InfaController {
    private Gson gson = new Gson();

    private final CharRepository charRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final RaceRepository raceRepository;
    private final InstanceRepository instanceRepository;


    @Autowired
    public InfaController(CharRepository characterRepository, UserRepository userRepository, ClassRepository classRepository, RaceRepository raceRepository, InstanceRepository instanceRepository) {
        this.charRepository = characterRepository;
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.raceRepository = raceRepository;
        this.instanceRepository = instanceRepository;
    }


    private void checkSession(Model model) {
        String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (!user.equals("anonymousUser")) {  //probably a better way to find out if nothing is online
            OAuth2Authentication principal = ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication());
            int idOfUser = Integer.valueOf(user);
            BattleUser bt = userRepository.findById(idOfUser).orElseGet(() -> initNewUser(principal));
            model.addAttribute("currentUser", bt); //doing this makes it so the viewer files can get some data out of the current user
        }
        model.addAttribute("users", userRepository.findAll());
    }

    @GetMapping("/")
    String root(Model model) {
        checkSession(model);
        model.addAttribute("wowChar", new WowChar());
        model.addAttribute("instances", instanceRepository.findAll());
        return "index";
    }


    @GetMapping("/update/{region}/{realm}/{name}")
    ModelAndView update(@PathVariable("region") String region, @PathVariable("realm") String realm, @PathVariable("name") String name, Model model) {

        return new ModelAndView("redirect:" + "/");
    }

    @GetMapping("/updateAll")
    ModelAndView updateAll(Model model) {
        int user = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        userRepository.findById(user).ifPresent(bt -> bt.getCharacters().forEach(WowChar::update));
        return new ModelAndView("redirect:" + "/");
    }


    @GetMapping("/battleUser/{id}")
    String updateAll(@PathVariable("id") Integer id, Model model) {
        BattleUser bt = userRepository.findById(id).orElse(null);
        model.addAttribute("battleUser", bt);
        checkSession(model);
        return "battleUser";
    }



    private BattleUser initNewUser(OAuth2Authentication principal) {
        LinkedHashMap userData = (LinkedHashMap) principal.getUserAuthentication().getDetails();
        final OAuth2AuthenticationDetails token = (OAuth2AuthenticationDetails) principal.getDetails();
        String battleTag = userData.get("battletag").toString();
        String id = userData.get("id").toString();
        BattleUser bt = new BattleUser(id, battleTag, token.getTokenValue());
        userRepository.save(bt);

        deepScan(bt);
        return bt;
    }

    /**
     * Doing a deepscan on each character for this user.
     * @param user the user who currently logged in for the first time.
     */
    private void deepScan(BattleUser user) {
        JsonArray apiData = ApiRequest.sendApiAccountRequest("eu", user.getAccessToken()).get("characters").getAsJsonArray();
        apiData.forEach(apiChar -> apiChar.getAsJsonObject().addProperty("region", "eu"));
        JsonArray filteredCharacters = new JsonArray();
        apiData.forEach(wowChar -> checkCharRules(user, wowChar.getAsJsonObject(), filteredCharacters));
        charRepository.saveAll(gson.fromJson(filteredCharacters, ApiRequest.getTypeToken(WowChar.class)));
        user.setCharacters(new ArrayList<>());
        user.getCharacters().addAll(charRepository.findAllByOwner(user));
        System.out.println(user.getCharacters().size());
        user.getCharacters().forEach(WowChar::update);
        userRepository.save(user);
    }


    /**
     * the rules for the character to be included to the list
     *
     * @param bt
     * @param wowChar
     * @param filteredCharacters
     */
    private void checkCharRules(BattleUser bt, JsonObject wowChar, JsonArray filteredCharacters) {
        if (wowChar.get("level").getAsInt() == 120) {
            wowChar.add("owner", gson.toJsonTree(bt));
            WowClass wowClass = classRepository.findById(wowChar.get("class").getAsInt()).orElse(null);
            WowRace wowRace = raceRepository.findById(wowChar.get("race").getAsInt()).orElse(null);
            wowChar.add("wowClass", gson.toJsonTree(wowClass));
            wowChar.add("wowRace", gson.toJsonTree(wowRace));
            filteredCharacters.add(wowChar);
        }
    }


}
