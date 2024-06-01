package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import br.com.fatex.backend_fatex.entities.Carona;

public interface CadastrarCaronaRepository extends CrudRepository<Carona, Integer>{
    
    @Query("SELECT c FROM Carona c WHERE c.motoristaVeiculo.motorista.motId = ?1 AND c.carStatus = 'Concluida'")
    List<Carona> findCaronasComoMotorista(int motoristaId);
}
