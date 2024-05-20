package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Usuario;

public interface CadastroUsuarioRepository extends CrudRepository<Usuario, Integer> {
    
}
