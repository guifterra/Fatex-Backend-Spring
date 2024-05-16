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
import br.com.fatex.backend_fatex.repository.Repository;
import br.com.fatex.backend_fatex.repository.RepositorySearch;

@RestController
@CrossOrigin(origins = "*")
public class Controler {
    
    @Autowired
    private Repository acao;

    @Autowired
    private RepositorySearch busca;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario u) {
        Usuario usuarioEncontrado = busca.findByUsuEmailAndUsuSenha(u.getUsuEmail(), u.getUsuSenha());
        if ((usuarioEncontrado) != null) {
            return ResponseEntity.ok(usuarioEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos");
        }
    }

    @PostMapping("/")
    public Usuario cadastrar( @RequestBody Usuario u ){
        return acao.save(u);
    }

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
