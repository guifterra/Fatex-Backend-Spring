package br.com.fatex.backend_fatex.jsonSeparator;

import br.com.fatex.backend_fatex.entities.Motorista;
import br.com.fatex.backend_fatex.entities.Veiculo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class JsonMotVei {
    
    @Valid
    @NotNull
    private Motorista motorista;

    @Valid
    @NotNull
    private Veiculo veiculo;

    public Motorista getMotorista(){

        return motorista;
    }

    public Veiculo getVeiculo(){

        return veiculo;
    }
}
