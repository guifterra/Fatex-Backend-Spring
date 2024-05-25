package br.com.fatex.backend_fatex.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
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

    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$", message = "A placa deve ter o formato AAA-1234")
    @Size(min = 8, max = 8, message = "A placa deve ter exatamente 8 caracteres")
    @NotBlank(message = "Placa é obrigatória")
    @Column(name = "VEI_PLACA", nullable = false, unique = true, length = 8)
    private String veiPlaca;

    @Column(name = "VEI_MAX_PASSAGEIROS", nullable = false)
    @Min(value = 1, message = "O máximo de passageiros deve ser maior que 0")
    private int veiMaxPassageiros;

    @Column(name = "VEI_COR", nullable = false, length = 255)
    @NotBlank(message = "A cor do veiculo é obrigatória")
    private String veiCor;

    @ManyToOne
    @JoinColumn(name = "MOD_ID", referencedColumnName = "MOD_ID", nullable = false)
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "MAR_ID", referencedColumnName = "MAR_ID", nullable = false)
    private Marca marca;
}

