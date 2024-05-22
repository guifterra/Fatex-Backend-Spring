package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Passageiro;

public interface CadastrarPassageiroRepository extends CrudRepository<Passageiro, Integer> {
    
}
