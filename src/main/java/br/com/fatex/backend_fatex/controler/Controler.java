package br.com.fatex.backend_fatex.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import br.com.fatex.backend_fatex.entities.*;
import br.com.fatex.backend_fatex.jsonSeparator.*;
import br.com.fatex.backend_fatex.repository.*;

@RestController
@CrossOrigin(origins = "*")
public class Controler {
    
    @Autowired
    private CadastroUsuarioRepository acao;

    @Autowired
    private LoginRepository login;

    @Autowired
    private CadastrarPassageiroRepository criarPassageiro;

    @Autowired
    private CadastrarMotoristaRepository criarMotorista;

    @Autowired
    private CadastrarVeiculoRepository criarVeiculo;

    @Autowired
    private VincularMotVeiRepository vincularMotVei;

    @Autowired
    private BuscaVeiculoRepository buscarVeiculo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());
        if ((usuarioEncontrado) != null) {
            return ResponseEntity.ok(usuarioEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        // Criando e salvando Usuario no banco
        Usuario novoUsuario = acao.save( usuario );

        // Criando e salvando a conta como Passageiro no banco
        criarContaPassageiro( novoUsuario );

        // Testando se há necessicade de criar conta Motorista
        if( novoUsuario.getUsuTipo().name().equalsIgnoreCase("MOTORISTA") ){
            criarContaMotorista( novoUsuario );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    public void criarContaPassageiro( Usuario usuario ){

        Passageiro passageiro = new Passageiro( usuario );
        criarPassageiro.save( passageiro );
    }

    public void criarContaMotorista( Usuario usuario ){

        Motorista novoMotorista = new Motorista( usuario );
        criarMotorista.save( novoMotorista );
    }

    @PostMapping("/cadastroVeiculo")
    public ResponseEntity<?> cadastrarVeiculo(@Valid @RequestBody JsonMotVei jsonMotVei, BindingResult result) {
        
        Veiculo veiculo = buscarVeiculo.findByVeiPlaca(jsonMotVei.getVeiculo().getVeiPlaca()).orElse(null);

        if( veiculo == null ){
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
            }

            criarVeiculo.save( jsonMotVei.getVeiculo() );
            return vincularMotoristaAoVeiculo( jsonMotVei );
        }

        return vincularMotoristaAoVeiculo( jsonMotVei );
    }

    public ResponseEntity<?> vincularMotoristaAoVeiculo(@Valid @RequestBody JsonMotVei jsonMotVei) {
        try {
            // Verifique se o veículo existe no banco de dados
            Veiculo veiculo = buscarVeiculo.findByVeiPlaca(jsonMotVei.getVeiculo().getVeiPlaca())
                    .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

            // Verifique se o motorista existe no banco de dados
            Motorista motorista = criarMotorista.findById(jsonMotVei.getMotorista().getMotId())
                    .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado"));

            // Crie o vínculo entre o motorista e o veículo
            MotoristaVeiculo novoVinculo = new MotoristaVeiculo(motorista, veiculo);
            vincularMotVei.save(novoVinculo);

            return ResponseEntity.status(HttpStatus.CREATED).body( veiculo );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vínculo: " + e.getMessage());
        }
    }


    // Exeplos (Apagar depois)
    @GetMapping("/")
    public Iterable<Usuario> selecionar(){
        return acao.findAll();
    }

    @PutMapping("/")
    public Usuario editar( @RequestBody Usuario u ){
        return acao.save(u);
    }

    @DeleteMapping("/{usu_id}")
    public void remover( @PathVariable Integer usu_id ){
        acao.deleteById( usu_id );
    }
}
