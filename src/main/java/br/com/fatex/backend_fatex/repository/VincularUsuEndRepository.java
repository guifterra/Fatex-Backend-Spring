package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Endereco;
import br.com.fatex.backend_fatex.entities.Usuario;
import br.com.fatex.backend_fatex.entities.UsuarioEndereco;

public interface VincularUsuEndRepository extends CrudRepository<UsuarioEndereco, UsuarioEndereco.UsuarioEnderecoId> {
 
    @Query("SELECT ue FROM UsuarioEndereco ue WHERE ue.usuario = ?1 AND ue.endereco = ?2")
    UsuarioEndereco findByUsuarioAndEndereco( Usuario usuario, Endereco endereco );
}
