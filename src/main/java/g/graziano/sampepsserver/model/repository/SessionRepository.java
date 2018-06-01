package g.graziano.sampepsserver.model.repository;


import g.graziano.sampepsserver.model.data.Child;
import g.graziano.sampepsserver.model.data.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findSessionsByChildId(Long id);

    List<Session> findByDateAfterAndChildId(Date endDate, Long id);

    @Transactional
    void deleteAllByChildId(Long id);

}
