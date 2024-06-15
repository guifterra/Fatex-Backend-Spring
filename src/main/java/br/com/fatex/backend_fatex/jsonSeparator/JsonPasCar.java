package br.com.fatex.backend_fatex.jsonSeparator;

import br.com.fatex.backend_fatex.entities.Carona;
import br.com.fatex.backend_fatex.entities.Passageiro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class JsonPasCar {
    
    @Valid
    @NotNull
    private Passageiro passageiro;

    @Valid
    @NotNull
    private Carona carona;

    public Passageiro getPassageiro(){

        return passageiro;
    }

    public Carona getCarona(){

        return carona;
    }
}
