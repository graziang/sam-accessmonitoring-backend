package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Integer> {

    Child findById(Long id);
    boolean existsById(Long id);
}
