package br.com.fatex.backend_fatex.jsonSeparator;

import br.com.fatex.backend_fatex.entities.Usuario;
import br.com.fatex.backend_fatex.entities.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class JsonUsuEnd {
    
    @Valid
    @NotNull
    private Usuario usuario;

    @Valid
    @NotNull
    private Endereco endereco;

    public Usuario getUsuario(){

        return usuario;
    }

    public Endereco getEndereco(){

        return endereco;
    }
}
