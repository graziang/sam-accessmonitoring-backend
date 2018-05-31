package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Measurement;
import g.graziano.sampepsserver.model.data.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    List<Measurement> findByDateBetween(Date startDate, Date endDate);

    List<Measurement> findByDateAfter(Date startDate);

    List<Measurement> findByDateBefore(Date endDate);


    List<Measurement> findByDateBetweenAndSensorId(Date startDate, Date endDate, Long id);

    List<Measurement> findByDateAfterAndSensorId(Date startDate, Long id);

    List<Measurement> findByDateBeforeAndSensorId(Date endDate, Long id);




    Measurement findFirstBySensorIdOrderByDateDesc(Long id);

}