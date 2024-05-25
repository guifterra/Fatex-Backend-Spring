package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.MotoristaVeiculo;

public interface VincularMotVeiRepository extends CrudRepository<MotoristaVeiculo, MotoristaVeiculo.MotoristaVeiculoId> {
    
}
