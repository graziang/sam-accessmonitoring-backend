package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Integer> {

    public boolean existsByName(String name);
}
