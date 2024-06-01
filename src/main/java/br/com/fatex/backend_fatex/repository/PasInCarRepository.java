package br.com.fatex.backend_fatex.repository;

import br.com.fatex.backend_fatex.entities.PassageiroCarona;
import br.com.fatex.backend_fatex.entities.Carona;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PasInCarRepository extends CrudRepository<PassageiroCarona, Integer>{
    
    @Query("SELECT pc.carona FROM PassageiroCarona pc WHERE pc.passageiro.id = ?1 AND pc.carona.carStatus = 'Concluida'")
    List<Carona> findCaronasComoPassageiro(int passageiroId);
}
