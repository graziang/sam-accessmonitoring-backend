package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {

}
