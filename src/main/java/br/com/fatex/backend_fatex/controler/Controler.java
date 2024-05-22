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
import br.com.fatex.backend_fatex.entities.Motorista;
import br.com.fatex.backend_fatex.entities.Passageiro;
import br.com.fatex.backend_fatex.entities.Usuario;
import br.com.fatex.backend_fatex.repository.CadastroUsuarioRepository;
import br.com.fatex.backend_fatex.repository.LoginRepository;
import br.com.fatex.backend_fatex.repository.CadastrarMotoristaRepository;
import br.com.fatex.backend_fatex.repository.CadastrarPassageiroRepository;

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
        Usuario novoUsuario = acao.save(usuario);

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

        Motorista motorista = new Motorista( usuario );
        criarMotorista.save( motorista );
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
