package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PAS_PASSAGEIROS")
@Getter
@Setter
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAS_ID")
    private int pasId;

    @Column(name = "PAS_NOTA", nullable = false)
    private BigDecimal pasNota;

    @Column(name = "PAS_QT_AVALIACOES", nullable = false)
    private int pasQtAvaliacoes;

    @OneToOne
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID", unique = true)
    private Usuario usuario;

    public Passageiro() { }

    public Passageiro( Usuario usuario ){
        setPasNota( new BigDecimal("0") );
        setUsuario( usuario );
        setPasQtAvaliacoes( 0 );
    }
}

