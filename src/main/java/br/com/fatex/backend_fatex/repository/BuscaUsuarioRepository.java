package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Usuario;
import java.util.Optional;

public interface BuscaUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuEmail(String usuEmail);
}
