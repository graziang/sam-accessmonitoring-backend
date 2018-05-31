package g.graziano.sampepsserver.service;

import g.graziano.sampepsserver.model.data.Child;
import g.graziano.sampepsserver.model.data.Family;
import g.graziano.sampepsserver.model.repository.ChildRepository;
import g.graziano.sampepsserver.model.repository.FamilyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class FamilyService {

    private static final Logger logger = LoggerFactory.getLogger(FamilyService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private ChildRepository childRepository;


    @PostConstruct
    public void init(){

    }

    public Family createFamily(Family family){

        if(familyRepository.existsByName(family.getName())){
            logger.error("Family not found: "  + family.getName());
            return null;
        }

        family.setPassword(passwordEncoder.encode(family.getPassword()));
        family.setChildrenPassword(passwordEncoder.encode(family.getChildrenPassword()));

        return this.familyRepository.save(family);
    }

    public Family getFamily(String familyName){
        return this.familyRepository.findByName(familyName);
    }

    public Child createChild(String familyName, Child child){

        if(!familyRepository.existsByName(familyName)) {
            logger.error("Family not found: "  + familyName);
            return null;
        }

        Family family = familyRepository.findByName(familyName);
        family.getChildren().add(child);
        child.setFamily(family);

        this.familyRepository.save(family);

        return child;
    }

    public Child getChild(Long childId){
        return this.childRepository.findById(childId);
    }


}
