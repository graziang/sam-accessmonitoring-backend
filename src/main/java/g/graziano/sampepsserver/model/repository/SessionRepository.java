package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findSessionsByChildId(Long id);

    List<Session> findByDateBeforeAndChildId(Date endDate, Long id);
}
