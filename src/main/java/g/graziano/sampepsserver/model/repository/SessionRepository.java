package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findSessionsByChildId(Long id);

    List<Session> findSessionsByDateHoursAfterAndChAndChildId(Integer hours, Long childId);

    List<Session> findSessionsByDateSecondsAfterAndChildIdAndChildLike(Integer seconds, Long childId);
}
