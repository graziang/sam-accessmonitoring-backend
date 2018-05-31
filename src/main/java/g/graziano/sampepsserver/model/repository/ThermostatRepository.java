package g.graziano.sampepsserver.model.repository;

import g.graziano.sampepsserver.model.data.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThermostatRepository  extends JpaRepository<Thermostat, Integer> {

    Thermostat findThermostatById(Long id);

}