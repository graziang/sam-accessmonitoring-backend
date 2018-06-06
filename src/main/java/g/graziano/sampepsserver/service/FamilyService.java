package g.graziano.sampepsserver.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import g.graziano.sampepsserver.exception.NotFoundException;
import g.graziano.sampepsserver.model.data.Child;
import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.data.Session;
import g.graziano.sampepsserver.model.repository.ChildRepository;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import g.graziano.sampepsserver.model.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FamilyService {

    private static final Logger logger = LoggerFactory.getLogger(FamilyService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @PostConstruct
    public void init(){

    }

    public Family createFamily(Family family) throws NotFoundException {

        if(familyRepository.existsByName(family.getName())){
            String errorMessage = "Family already exist: [name: "  + family.getName() + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        family.setPassword(passwordEncoder.encode(family.getPassword()));
        family.setChildrenPassword(passwordEncoder.encode(family.getChildrenPassword()));

        return this.familyRepository.save(family);
    }


    public Family getFamily(String familyName) {
        return this.familyRepository.findByName(familyName);
    }

    public Family getFamily(String familyName, String password) throws NotFoundException {


        Family family = null;

        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }


        family = this.familyRepository.findByName(familyName);
        if(!passwordEncoder.matches(password, family.getPassword())) {
            String errorMessage = "Bad family password: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        for (Child child: family.getChildren()){
            child.setLastSession(this.sessionRepository.findTopByChildIdOrderByDateDesc(child.getId()));
        }

        return family;

    }

    public void deleteFamily(String familyName) throws NotFoundException {

        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        Family family = familyRepository.findByName(familyName);

        for (Child child: family.getChildren()){
            List<Session> session = this.sessionRepository.findSessionsByChildId(child.getId());
            this.sessionRepository.deleteAll(session);

        }

        this.childRepository.deleteAll(family.getChildren());
        this.familyRepository.deleteByName(familyName);
    }

    public Child createChild(String familyName, Child child, String password) throws NotFoundException {

        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }


        Family family = this.familyRepository.findByName(familyName);

        if(!passwordEncoder.matches(password, family.getChildrenPassword())) {

            String errorMessage = "Bad child password: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if(childRepository.existsByNameAndFamilyId(child.getName(), family.getId())) {
            String errorMessage = "Child already exist: [child_name: "  + child.getName() + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        family.getChildren().add(child);
        child.setFamily(family);

        this.familyRepository.save(family);

        return this.childRepository.findByNameAndFamilyId(child.getName(), family.getId());
    }

    public void deleteChild(String familyName, Long id) throws NotFoundException {

        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        Family family = familyRepository.findByName(familyName);

        Child child = this.childRepository.findByNameAndFamilyId(family.getName(), id);

        this.sessionRepository.deleteAllByChildId(child.getId());

        this.childRepository.deleteByNameAndFamilyId(child.getName(), family.getId());

    }

    public Child getChild(String familyName, String childName) throws NotFoundException {


        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        Family family = this.familyRepository.findByName(familyName);



        if(!childRepository.existsByNameAndFamilyId(childName, family.getId())){
            String errorMessage = "Child not found: [child_name: "  + childName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return this.childRepository.findByNameAndFamilyId(childName, family.getId());
    }

    public Child getChild(String familyName, String childName, String password) throws NotFoundException {


        if(!familyRepository.existsByName(familyName)) {
            String errorMessage = "Family not found: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }


        Family family = this.familyRepository.findByName(familyName);

        if(!childRepository.existsByNameAndFamilyId(childName, family.getId())){
            String errorMessage = "Child not found: [child_name: "  + childName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if(!passwordEncoder.matches(password, family.getChildrenPassword())) {

            String errorMessage = "Bad child password: [family_name: "  + familyName + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return this.childRepository.findByNameAndFamilyId(childName, family.getId());
    }

    public Session createSession(String familyName, String childName, Session session) throws NotFoundException {

        Child child = this.getChild(familyName, childName);

        session.setDate(new Date());
        session.setChild(child);

       return this.sessionRepository.save(session);
    }

    public List<Session> getSessions(Long childId) throws NotFoundException {


        if(!childRepository.existsById(childId)) {
            String errorMessage = "Child not found: [child_id: "  + childId + "]";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        long hourMillis = 1000 * 60 * 60;

        Date oldDate = new Date(System.currentTimeMillis() - (24 * hourMillis));
        return this.sessionRepository.findByDateAfterAndChildId(oldDate, childId);
    }

    public Family setFamilyStatus(String familyName, String password, boolean status) throws NotFoundException {


        Family family = this.getFamily(familyName, password);

        family.setActive(status);

        this.familyRepository.save(family);

        return family;

    }

    public Child setChildStatus(String familyName, String childName, String password, boolean status) throws NotFoundException {



        Child child = this.getChild(familyName, childName, password);
        child.setActive(status);

        this.childRepository.save(child);

        return child;

    }

    public Family setChildrenPassword(String familyName, String password, String newChildrenPassword) throws NotFoundException {


        Family family = this.getFamily(familyName, password);

        family.setChildrenPassword(passwordEncoder.encode(newChildrenPassword));

        this.familyRepository.save(family);

        return family;

    }

}
