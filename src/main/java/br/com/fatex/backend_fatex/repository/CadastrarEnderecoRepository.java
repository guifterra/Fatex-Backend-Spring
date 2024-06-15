package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Endereco;

public interface CadastrarEnderecoRepository extends CrudRepository<Endereco, Integer> {
    
    @Query("SELECT e FROM Endereco e WHERE e.endLatitude = ?1 AND e.endLongitude = ?2")
    Endereco findEndereco(double latitude, double longitude);
}
