package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "USU_HAS_END")
@Getter
@Setter
public class UsuarioEndereco {

    @EmbeddedId
    private UsuarioEnderecoId id = new UsuarioEnderecoId();

    @ManyToOne
    @MapsId("usuId")
    @JoinColumn(name = "USU_ID")
    private Usuario usuario;

    @ManyToOne
    @MapsId("endId")
    @JoinColumn(name = "END_ID")
    private Endereco endereco;

    public UsuarioEndereco() { }

    public UsuarioEndereco( Usuario usuario, Endereco endereco ){
        setUsuario(usuario);
        setEndereco(endereco);
    }

    @Getter
    @Setter
    public static class UsuarioEnderecoId implements Serializable {
        private int usuId;
        private int endId;
    }
}

