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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.PrePersist;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USU_USUARIO")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USU_ID")
    private int usuId;

    @Email(regexp = ".+@fatec\\.sp\\.gov\\.br", message = "Email deve terminar em @fatec.sp.gov.br")
    @Size(min = 6, message = "Email deve ter no mínimo 6 caracteres")
    @NotBlank(message = "Email é obrigatório")
    @Column(name = "USU_EMAIL")
    private String usuEmail;

    @Pattern(regexp = "(?=.*[A-Z])(?=.*[@#$%^&+=-_]).{6,}", message = "Senha deve conter 1 caracter maiúsculo, tamanho mínimo de 6 e um caracter especial")
    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "USU_SENHA")
    private String usuSenha;

    @Size(min = 6, message = "Nome deve ter no mínimo 6 caracteres")
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "USU_NOME")
    private String usuNome;

    @Column(name = "USU_DT_NASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date usuDtNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "USU_GENERO")
    private Genero usuGenero;

    @Enumerated(EnumType.STRING)
    @Column(name = "USU_TIPO")
    private TipoUsuario usuTipo;

    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 caracteres e conter apenas números")
    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "USU_CPF", unique = true)
    private String usuCpf;

    @PrePersist
    private void prePersist() {
        if (this.usuNome == null || this.usuNome.isEmpty()) {
            this.usuNome = "Usuário";
        }

        if (this.usuTipo == null) {
            this.usuTipo = TipoUsuario.valueOf("PASSAGEIRO");
        }
    }
}
