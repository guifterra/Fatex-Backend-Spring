package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatex.backend_fatex.entities.Veiculo;
import java.util.Optional;

public interface BuscaVeiculoRepository extends JpaRepository<Veiculo, Integer> {
    Optional<Veiculo> findByVeiPlaca(String veiPlaca);
}
