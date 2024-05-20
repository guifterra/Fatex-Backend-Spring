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
@Table(name = "MOT_MOTORISTAS")
@Getter
@Setter
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOT_ID")
    private int motId;

    @Column(name = "MOT_NOTA")
    private BigDecimal motNota;

    @Column(name = "MOT_CNH", length = 11)
    private String motCnh;

    @OneToOne
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID", unique = true)
    private Usuario usuario;
}

