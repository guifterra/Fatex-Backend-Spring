package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Motorista;
import br.com.fatex.backend_fatex.entities.Usuario;

public interface IdentificarMotoristaRepository extends JpaRepository<Motorista, Integer> {
    Motorista findByUsuario(Usuario usuario);
}
