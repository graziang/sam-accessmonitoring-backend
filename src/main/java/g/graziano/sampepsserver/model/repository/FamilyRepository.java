package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Integer> {

    boolean existsByName(String name);
    Family findByName(String name);

    @Transactional
    void deleteByName(String name);

    Family findByNameAndPassword(String name, String password);

    Family findByNameAndChildrenPassword(String name, String password);
}
