package g.graziano.sampepsserver.controller;


import g.graziano.sampepsserver.exception.NotFoundException;
import g.graziano.sampepsserver.model.data.Child;
import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.data.Session;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import g.graziano.sampepsserver.service.AndroidNotificationsService;
import g.graziano.sampepsserver.service.FamilyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);


    @Autowired
    FamilyService familyService;

    @Autowired
    AndroidNotificationsService androidNotificationsService;


    @GetMapping("/family")
    public ResponseEntity getFamily( @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password){

        Family family = null;
        try {
            family = familyService.getFamily(familyName, password);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }

        if(family == null){
            return this.getError("Family not found: [family_name: " + familyName + "]", HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }
        return new ResponseEntity(family, HttpStatus.OK);
    }

    @PostMapping("/family")
    public ResponseEntity createFamily(@Valid @RequestBody Family family){

        Family newFamily = null;
        try {
            newFamily = familyService.createFamily(family);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }


        return new ResponseEntity(newFamily, HttpStatus.OK);
    }


    @PutMapping("/update_children_password")
    public ResponseEntity updateChildrenPassord(@RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password, @Valid @RequestParam(value = "new_children_password", required = true) String newChildrenPassowrd){


        Family family = null;
        try {
            family = familyService.setChildrenPassword(familyName, password, newChildrenPassowrd);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }

        return new ResponseEntity(family, HttpStatus.OK);
    }

    @PutMapping("/active_family")
    public ResponseEntity updateFamilyStatus(@RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password, @Valid @RequestParam(value = "active", required = true) boolean active){

        Family family = null;
        try {
            family = familyService.setFamilyStatus(familyName, password, active);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }

        return new ResponseEntity(family, HttpStatus.OK);
    }

    @PutMapping("/active_child")
    public ResponseEntity updateChildStatus(@RequestParam(value = "child_name", required = true) String childName, @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password, @Valid @RequestParam(value = "active", required = true) boolean active){

        Child child = null;
        try {
            child = familyService.setChildStatus(familyName, childName, password, active);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Child.class.getSimpleName());
        }

        return new ResponseEntity(child, HttpStatus.OK);
    }

    @DeleteMapping("/family")
    public ResponseEntity deleteFamily( @RequestParam(value = "family_name", required = true) String familyName){


        try {
            this.familyService.deleteFamily(familyName);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Family.class.getSimpleName());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/child")
    public ResponseEntity createChild(@Valid @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password, @Valid @RequestBody Child child){

        Child newChild = null;
        try {
            newChild = familyService.createChild(familyName, child, password);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Child.class.getSimpleName());
        }

        return new ResponseEntity(newChild, HttpStatus.OK);
    }

    @DeleteMapping("/child")
    public ResponseEntity deleteChild(@RequestParam(value = "child_id", required = true) Long childId, @RequestParam(value = "family_name", required = true) String familyName){


        try {
            this.familyService.deleteChild(familyName, childId);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Child.class.getSimpleName());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/child")
    public ResponseEntity getChild(@RequestParam(value = "child_name", required = true) String childName, @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "password", required = true) String password){

        Child child = null;
        try {
            child = familyService.getChild(familyName, childName, password);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Child.class.getSimpleName());
        }

        return new ResponseEntity(child, HttpStatus.OK);
    }

    @PostMapping("/session")
    public ResponseEntity createSession(@Valid @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestParam(value = "child_name", required = true) String childName, @RequestBody Session session){

        try {
            session = this.familyService.createSession(familyName, childName, session);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Session.class.getSimpleName());
        }

        List sessions = new ArrayList();
        sessions.add(session);

        try {
            androidNotificationsService.senddVWithSDK(familyService.getChild(familyName, childName));
        } catch (NotFoundException e) {
        }

        return new ResponseEntity(sessions, HttpStatus.OK);
    }

    @GetMapping("/session")
    public ResponseEntity getSessions(@Valid @RequestParam(value = "child_id", required = true) Long childId){

        List<Session> sessions = null;
        try {
            sessions = this.familyService.getSessions(childId);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST, Session.class.getSimpleName());
        }
        return new ResponseEntity(sessions, HttpStatus.OK);
    }



    public ResponseEntity getError(String error, HttpStatus status, String className){

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("classname", className);

        Map responsError = new HashMap();
        responsError.put("error", error);
        return new ResponseEntity<>(responsError, responseHeaders, status);
    }
}
