package com.GPOslo.airbnbclone.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping("/")
    public List<String> index() {
        System.out.println("여기 오긴 하니?");
        return List.of("hello", "bye");
    }
}
