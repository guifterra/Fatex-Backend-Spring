package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Veiculo;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CadastrarVeiculoRepository extends CrudRepository<Veiculo, Integer> {
    
    @Query("SELECT v FROM Veiculo v WHERE v.id = ?1")
    List<Veiculo> findVeiculosComIdPar(int id);
}
