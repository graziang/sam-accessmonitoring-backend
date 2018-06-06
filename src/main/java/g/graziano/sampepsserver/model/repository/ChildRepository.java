package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ChildRepository extends JpaRepository<Child, Integer> {

    Child findById(Long id);
    boolean existsById(Long id);


    Child findByNameAndFamilyId(String name, Long id);

    boolean existsByNameAndFamilyId(String name, Long id);


    @Transactional
    void deleteByNameAndFamilyId(String name, Long id);
}
