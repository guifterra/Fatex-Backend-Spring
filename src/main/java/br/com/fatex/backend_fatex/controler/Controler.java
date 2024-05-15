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

import br.com.fatex.backend_fatex.entities.Usuario;
import br.com.fatex.backend_fatex.repository.Repository;

@RestController
@CrossOrigin(origins = "*")
public class Controler {
    
    @Autowired
    private Repository acao;

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
