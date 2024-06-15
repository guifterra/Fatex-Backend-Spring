package br.com.fatex.backend_fatex.repository;

import br.com.fatex.backend_fatex.entities.UsuarioEndereco;
import br.com.fatex.backend_fatex.entities.Endereco;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UsuHasEndRepository extends CrudRepository<UsuarioEndereco, UsuarioEndereco.UsuarioEnderecoId> {
    
    @Query("SELECT ue.endereco FROM UsuarioEndereco ue WHERE ue.usuario.id = ?1 AND uheStatus = 'ATIVO'")
    List<Endereco> findEnderecosDoUsuario(int usuId);
}
