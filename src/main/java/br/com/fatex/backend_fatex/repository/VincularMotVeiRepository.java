package br.com.fatex.backend_fatex.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Motorista;
import br.com.fatex.backend_fatex.entities.MotoristaVeiculo;
import br.com.fatex.backend_fatex.entities.Veiculo;

public interface VincularMotVeiRepository extends CrudRepository<MotoristaVeiculo, MotoristaVeiculo.MotoristaVeiculoId> {
    
    @Query("SELECT mv FROM MotoristaVeiculo mv WHERE mv.veiculo = ?1 AND mv.motorista = ?2")
    MotoristaVeiculo findByVeiculoAndMotorista( Veiculo veiculo, Motorista motorista );
}
