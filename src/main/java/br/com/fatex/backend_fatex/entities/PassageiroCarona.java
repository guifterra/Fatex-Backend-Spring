package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PAS_IN_CAR")
@Getter
@Setter
public class PassageiroCarona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PIC_ID")
    private int picId;

    @ManyToOne
    @JoinColumn(name = "PAS_ID", nullable = false)
    private Passageiro passageiro;

    @ManyToOne
    @JoinColumn(name = "CAR_ID", nullable = false)
    private Carona carona;
}

