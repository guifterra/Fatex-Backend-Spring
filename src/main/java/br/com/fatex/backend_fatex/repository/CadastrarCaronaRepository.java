package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import br.com.fatex.backend_fatex.entities.Carona;

public interface CadastrarCaronaRepository extends CrudRepository<Carona, Integer>{
    
    @Query("SELECT c FROM Carona c WHERE c.motoristaVeiculo.motorista.motId = ?1")
    List<Carona> findCaronasComoMotorista(int motoristaId);

    @Query("SELECT c FROM Carona c WHERE c.carStatus = 'Agendada' AND c.motoristaVeiculo IS NULL AND DATE(c.carData) = DATE(NOW()) AND TIME(c.carHora) > TIME(NOW())")
    List<Carona> findCaronasSolicitadas();

    @Query("SELECT c FROM Carona c WHERE c.id = ?1")
    Carona findCarona(int caronaId);

    @Query("SELECT c FROM Carona c WHERE c.carStatus = 'Agendada' AND (c.motoristaVeiculo IS NOT NULL) AND  DATE(c.carData) = DATE(NOW()) AND TIME(c.carHora) > TIME(NOW())")
    List<Carona> findOferecerCarona();
}
