package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "MOT_HAS_VEI")
@Getter
@Setter
public class MotoristaVeiculo {

    @EmbeddedId
    private MotoristaVeiculoId id = new MotoristaVeiculoId();

    @ManyToOne
    @MapsId("motId")
    @JoinColumn(name = "MOT_ID")
    private Motorista motorista;

    @ManyToOne
    @MapsId("veiId")
    @JoinColumn(name = "VEI_ID")
    private Veiculo veiculo;

    @Column(name = "MHV_STATUS")
    @Enumerated(EnumType.STRING)
    private Visibilidade mhvStatus = Visibilidade.ATIVO;

    public MotoristaVeiculo() { }

    public MotoristaVeiculo( Motorista motorista, Veiculo veiculo ){
        setMotorista( motorista );
        setVeiculo( veiculo );
        this.mhvStatus = Visibilidade.ATIVO;
    }

    @Getter
    @Setter
    public static class MotoristaVeiculoId implements Serializable {
        private int motId;
        private int veiId;
    }

}

