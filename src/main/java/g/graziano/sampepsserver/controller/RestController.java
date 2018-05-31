package g.graziano.sampepsserver.controller;


import g.graziano.sampepsserver.exception.NotFoundException;
import g.graziano.sampepsserver.model.data.Child;
import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.data.Session;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import g.graziano.sampepsserver.service.FamilyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    FamilyService familyService;


    @GetMapping("/family")
    public ResponseEntity getFamily( @RequestParam(value = "family_name", required = true) String familyName){

        Family family = familyService.getFamily(familyName);

        if(family == null){
            return this.getError("Family not found:" + familyName, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(family, HttpStatus.OK);
    }

    @PostMapping("/family")
    public ResponseEntity createFamily(@Valid @RequestBody Family family){

        Family newFamily = null;
        try {
            newFamily = familyService.createFamily(family);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(newFamily, HttpStatus.OK);
    }

    @PostMapping("/child")
    public ResponseEntity createChild(@Valid @RequestParam(value = "family_name", required = true) String familyName, @Valid @RequestBody Child child){

        Child newChild = null;
        try {
            newChild = familyService.createChild(familyName, child);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(newChild, HttpStatus.OK);
    }

    @GetMapping("/child")
    public ResponseEntity getChild( @RequestParam(value = "child_id", required = true) Long childID){

        Child child = familyService.getChild(childID);

        if(child == null){
            return this.getError("Child not found: [id: " + childID + "]", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(child, HttpStatus.OK);
    }

    @PostMapping("/session")
    public ResponseEntity createSession(@Valid @RequestParam(value = "child_id", required = true) Long childId, @RequestBody Session session){

        try {
            session = this.familyService.createSession(childId, session);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(session, HttpStatus.OK);
    }

    @GetMapping("/session")
    public ResponseEntity getSessions(@Valid @RequestParam(value = "child_id", required = true) Long childId){

        List<Session> sessions = null;
        try {
            sessions = this.familyService.getSessions(childId);
        } catch (NotFoundException e) {
            return this.getError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(sessions, HttpStatus.OK);
    }



    public ResponseEntity getError(String error, HttpStatus status){
        Map responsError = new HashMap();
        responsError.put("error", error);
        return new ResponseEntity<>(responsError, status);
    }
}
