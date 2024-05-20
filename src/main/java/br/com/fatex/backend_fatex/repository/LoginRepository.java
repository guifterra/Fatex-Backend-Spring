package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Usuario;

public interface LoginRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsuEmailAndUsuSenha(String usuEmail, String usuSenha);
}
