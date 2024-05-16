package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USU_USUARIO")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "USU_ID")
    private int usuId;

    @Column(name = "USU_EMAIL")
    private String usuEmail;

    @Column(name = "USU_SENHA")
    private String usuSenha;

    @Column(name = "USU_NOME")
    private String usuNome;

    @Column(name = "USU_DT_NASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date usuDtNascimento;

    @Column(name = "USU_GENERO")
    @Enumerated(EnumType.STRING)
    private Genero usuGenero;

    @Column(name = "USU_CPF", unique = true)
    private String usuCpf;
}
