package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.UsuarioEndereco;

public interface VincularUsuEndRepository extends CrudRepository<UsuarioEndereco, UsuarioEndereco.UsuarioEnderecoId> {
    
}
