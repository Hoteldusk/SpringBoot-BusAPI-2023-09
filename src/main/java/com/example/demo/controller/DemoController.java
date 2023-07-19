package com.example.demo.controller;

import com.example.demo.service.SaveDB;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    private final SaveDB saveDB;
    @Autowired
    public DemoController(SaveDB saveDB) {
        this.saveDB = saveDB;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/spring")
    public String spring() {
        return "spring";
    }

    @GetMapping("/saveDB")
    public String saveDB() throws JSONException {
        saveDB.SaveTerDB();
        saveDB.SaveTerLinkDB();
        saveDB.SaveTerDriveDB();
        saveDB.SaveTerScheduleDB();

        System.out.println("DB 저장완료");
        
        return "redirect:/";
    }
}
