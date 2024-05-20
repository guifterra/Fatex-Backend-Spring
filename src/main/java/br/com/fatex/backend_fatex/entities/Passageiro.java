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

    @Column(name = "PAS_NOTA")
    private BigDecimal pasNota;

    @OneToOne
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID", unique = true)
    private Usuario usuario;
}

