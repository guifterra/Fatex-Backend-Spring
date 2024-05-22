package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Motorista;

public interface CadastrarMotoristaRepository extends CrudRepository<Motorista, Integer> {
    
}
