package g.graziano.sampepsserver.controller;


import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    FamilyRepository familyRepository;

    @GetMapping("/test")
    public List<Family> test(){

        return familyRepository.findAll();
    }

    @PostMapping("/test")
    public ResponseEntity testno(@RequestParam(value = "name", required = true) String name){

        if(familyRepository.existsByName("test")){
            return new ResponseEntity<>("Name: " + name + " already used", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(familyRepository.save(new Family(name, "desc", true)), HttpStatus.OK);
    }
}
