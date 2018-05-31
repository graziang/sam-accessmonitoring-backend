package g.graziano.sampepsserver.controller;


import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {


    @Autowired
    FamilyRepository familyRepository;

    @GetMapping("/test")
    public List<Family> test(){

        return familyRepository.findAll();
    }

    @PostMapping("/test")
    public Family testno(){

        return familyRepository.save(new Family("test", "desc", true));
    }
}
