package br.com.fatex.backend_fatex.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fatex.backend_fatex.entities.Marca;

public interface MarcaRepository extends CrudRepository<Marca, Integer>{
    
}
