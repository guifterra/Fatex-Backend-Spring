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

import br.com.fatex.backend_fatex.entities.Usuario;
import br.com.fatex.backend_fatex.repository.CadastroUsuarioRepository;
import br.com.fatex.backend_fatex.repository.LoginRepository;

@RestController
@CrossOrigin(origins = "*")
public class Controler {
    
    @Autowired
    private CadastroUsuarioRepository acao;

    @Autowired
    private LoginRepository login;

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
    public Usuario cadastrar( @RequestBody Usuario usuario ){
        return acao.save(usuario);
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
