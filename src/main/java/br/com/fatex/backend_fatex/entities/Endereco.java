package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "END_ENDERECOS")
@Getter
@Setter
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "END_ID")
    private int endId;

    @Column(name = "END_NUMERO", nullable = false)
    private int endNumero;

    @Column(name = "END_RUA", nullable = false, length = 255)
    private String endRua;

    @Column(name = "END_BAIRRO", nullable = false, length = 255)
    private String endBairro;

    @Column(name = "END_CIDADE", nullable = false, length = 255)
    private String endCidade;

    @Column(name = "END_LATITUDE")
    private Double endLatitude;

    @Column(name = "END_LONGITUDE")
    private Double endLongitude;
}

