package br.com.fatex.backend_fatex.repository;

import br.com.fatex.backend_fatex.entities.PassageiroCarona;
import br.com.fatex.backend_fatex.entities.Carona;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PasInCarRepository extends CrudRepository<PassageiroCarona, Integer>{
    
    @Query("SELECT pc.carona FROM PassageiroCarona pc WHERE pc.passageiro.id = ?1 AND pc.carona.carStatus = 'Concluida'")
    List<Carona> findCaronasComoPassageiro(int passageiroId);

    @Query("SELECT COUNT(pc.carona.id) FROM PassageiroCarona pc WHERE pc.carona.id = ?1")
    Integer identificarVagasOcupadas(int caronaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PassageiroCarona pc WHERE pc.carona.id = ?2 AND pc.passageiro.id = ?1")
    void removerPassageiroDaCarona(int passageiroId, int caronaId);
}
