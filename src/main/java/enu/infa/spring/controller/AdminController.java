package enu.infa.spring.controller;


import com.google.gson.Gson;
import enu.infa.spring.ApiRequest;
import enu.infa.spring.model.*;
import enu.infa.spring.model.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import static enu.infa.spring.ApiRequest.Region.EU;
import static enu.infa.spring.ApiRequest.Request.REALM;
import static enu.infa.spring.ApiRequest.Request.MOUNT;

import static enu.infa.spring.ApiRequest.getTypeToken;
import static enu.infa.spring.ApiRequest.sendApiRequest;


@Controller
@RequestMapping(path = "admin", method = RequestMethod.GET)
public class AdminController {

    Gson gson = new Gson();

    private final ClassRepository classRepository;
    private final MountRepository mountRepository;

    private final RealmRepository realmRepository;

    public AdminController(ClassRepository classRepository, RealmRepository realmRepository, MountRepository mountRepository) {
        this.classRepository = classRepository;
        this.realmRepository = realmRepository;
        this.mountRepository = mountRepository;
    }


    @GetMapping("")
    String root(Model model) {
        return "admin";
    }

    private String getToken() {
        OAuth2Authentication principal = ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication());

        return  ((OAuth2AuthenticationDetails) principal.getDetails()).getTokenValue();
    }


    @PostMapping(value = "/realms")
    String Realm(Model model) {


        realmRepository.saveAll(gson.fromJson(sendApiRequest(EU, REALM, getToken()).get("realms").getAsJsonArray(), getTypeToken(WowRealm.class)));
        return "redirect:/admin";
    }


    @PostMapping(value = "/classes")
    String Classes(Model model) {

        //  JsonArray classArray = sendApiRequest(EU, CLASSES).get("classes").getAsJsonArray();
        //   classArray.forEach(wowClass -> wowClass.getAsJsonObject().addProperty("color", WowClass.Class.getValue(wowClass.getAsJsonObject().get("name").getAsString())));
        //   classRepository.saveAll(gson.fromJson(classArray, ApiRequest.getTypeToken(WowClass.class)));
        return "redirect:/admin";
    }


    @PostMapping(value = "/races")
    String Race(Model model) {
        //   classRepository.saveAll(gson.fromJson(sendApiRequest(EU, RACES).get("races").getAsJsonArray(), ApiRequest.getTypeToken(WowRace.class)));
        return "redirect:/admin";
    }


    @PostMapping(value = "/mounts")
    String Mount(Model model) {
        mountRepository.saveAll(gson.fromJson(sendApiRequest(EU, MOUNT, getToken()).get("mounts").getAsJsonArray(), ApiRequest.getTypeToken(WowMount.class)));
        return "redirect:/admin";
    }
}
