package br.com.fatex.backend_fatex.repository;

import br.com.fatex.backend_fatex.entities.MotoristaVeiculo;
import br.com.fatex.backend_fatex.entities.Veiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MotHasVeiRepository extends CrudRepository<MotoristaVeiculo, MotoristaVeiculo.MotoristaVeiculoId> {
    
    @Query("SELECT mv.veiculo FROM MotoristaVeiculo mv WHERE mv.motorista.id = ?1 AND mhvStatus = 'ATIVO'")
    List<Veiculo> findVeiculosDoMotorista(int motoristaId);

    @Query("SELECT mv FROM MotoristaVeiculo mv WHERE mv.motorista.id = ?1 AND mv.veiculo.id = ?2")
    MotoristaVeiculo findMotVei(int motoristaId, int veiculoId);
}
