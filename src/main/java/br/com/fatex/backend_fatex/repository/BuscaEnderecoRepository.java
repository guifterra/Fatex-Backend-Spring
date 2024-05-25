package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Endereco;

public interface BuscaEnderecoRepository extends JpaRepository<Endereco, Integer> {
    
}
