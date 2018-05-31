package g.graziano.sampepsserver.model.repository;


import g.graziano.termostato.model.data.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    Sensor findSensorById(Long id);

}