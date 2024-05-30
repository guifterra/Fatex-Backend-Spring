package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Passageiro;
import br.com.fatex.backend_fatex.entities.Usuario;

public interface IdentificarPassageiroRepository extends JpaRepository<Passageiro, Integer> {
    Passageiro findByUsuario(Usuario usuario);
}
