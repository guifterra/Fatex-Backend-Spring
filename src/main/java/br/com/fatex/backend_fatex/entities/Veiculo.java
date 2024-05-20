package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "VEI_VEICULOS")
@Getter
@Setter
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VEI_ID")
    private int veiId;

    @Column(name = "VEI_PLACA", nullable = false, unique = true, length = 11)
    private String veiPlaca;

    @Column(name = "VEI_MAX_PASSAGEIROS", nullable = false)
    private int veiMaxPassageiros;

    @Column(name = "VEI_COR", nullable = false, length = 255)
    private String veiCor;

    @ManyToOne
    @JoinColumn(name = "MOD_ID", referencedColumnName = "MOD_ID", nullable = false)
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "MAR_ID", referencedColumnName = "MAR_ID", nullable = false)
    private Marca marca;
}

