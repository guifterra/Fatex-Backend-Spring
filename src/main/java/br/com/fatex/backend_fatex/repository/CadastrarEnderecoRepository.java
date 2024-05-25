package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Endereco;

public interface CadastrarEnderecoRepository extends CrudRepository<Endereco, Integer> {
    
}
