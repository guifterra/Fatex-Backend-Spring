package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;

@Entity
@Table(name = "CAR_CARONAS")
@Getter
@Setter
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_ID")
    private int carId;

    @Column(name = "CAR_DATA", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate carData;

    @Column(name = "CAR_HORA", nullable = false)
    private String carHora;

    @ManyToOne
    @JoinColumn(name = "CAR_PARTIDA", referencedColumnName = "END_ID", nullable = false)
    private Endereco carPartida;

    @ManyToOne
    @JoinColumn(name = "CAR_CHEGADA", referencedColumnName = "END_ID", nullable = false)
    private Endereco carChegada;

    @Column(name = "CAR_VALOR_DOACAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal carValorDoacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAR_STATUS", nullable = false)
    private StatusCarona carStatus;

    @Column(name = "CAR_VAGAS")
    private int carVagas;

    @Column(name = "CAR_VALOR_MINIMO", precision = 10, scale = 2)
    private BigDecimal carValorMinimo;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "MOT_ID", referencedColumnName = "MOT_ID"),
        @JoinColumn(name = "VEI_ID", referencedColumnName = "VEI_ID")
    })
    private MotoristaVeiculo motoristaVeiculo;
}

