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
import br.com.fatex.backend_fatex.services.*;

import java.util.ArrayList;
import java.util.List;

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
    private CadastrarEnderecoRepository criarEndereco;

    @Autowired
    private CadastrarCaronaRepository criarCarona;

    @Autowired
    private VincularMotVeiRepository vincularMotVei;

    @Autowired
    private VincularUsuEndRepository vincularUsuEnd;

    @Autowired
    private BuscaVeiculoRepository buscarVeiculo;

    @Autowired
    private BuscaUsuarioRepository buscarUsuario;

    @Autowired
    private BuscaEnderecoRepository buscarEndereco;

    @Autowired
    private ModeloRepository modelo;

    @Autowired
    private MarcaRepository marca;

    @Autowired
    private MotHasVeiRepository identificarVeiculos;

    @Autowired
    private UsuHasEndRepository identificarEnderecos;

    @Autowired
    private IdentificarPassageiroRepository identificarPassageiro;

    @Autowired
    private IdentificarMotoristaRepository identificarMotorista;

    @Autowired
    private PasInCarRepository identificarCarona;

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private ReverseGeocodingService reverseGeocodingService;

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

    @PostMapping("/caronaPontos")
    public CaronaPontos caronaPontos(@RequestBody CaronaPontos caronaPontos) {
        return caronaPontos;
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

    @PostMapping("/cadastroEndereco")
    public ResponseEntity<?> cadastrarEndereco(@Valid @RequestBody JsonUsuEnd jsonUsuEnd, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        String enderecoEmString = String.format("%d %s, %s, %s SP", 
        jsonUsuEnd.getEndereco().getEndNumero(), 
        jsonUsuEnd.getEndereco().getEndRua(), 
        jsonUsuEnd.getEndereco().getEndBairro(), 
        jsonUsuEnd.getEndereco().getEndCidade());

        String dadosGeograficos = getCoordinates( enderecoEmString );
        
        if (dadosGeograficos != null && dadosGeograficos.contains(",")) {
            String[] coordenadas = dadosGeograficos.split(",");
            try {
                double latitude = Double.parseDouble(coordenadas[0].trim());
                double longitude = Double.parseDouble(coordenadas[1].trim());

                jsonUsuEnd.getEndereco().setEndLatitude(latitude);
                jsonUsuEnd.getEndereco().setEndLongitude(longitude);

                Endereco novoEndereco = criarEndereco.save(jsonUsuEnd.getEndereco());
                return vincularUsuarioAoEndereco(jsonUsuEnd, novoEndereco);
            } catch (NumberFormatException e) {
                // Erro no formato da coordenada
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de coordenadas inválido.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados geográficos inválidos.");
        }
    }

    public String getCoordinates(String address) {
        return geocodingService.getCoordinates(address);
    }

    public String getAddress(double lat, double lng) {
        return reverseGeocodingService.getAddress(lat, lng);
    }

    public ResponseEntity<?> vincularUsuarioAoEndereco(@Valid @RequestBody JsonUsuEnd jsonUsuEnd, Endereco novoEndereco) {
        try {
            // Verifique se o usuario existe no banco de dados
            Usuario usuario = buscarUsuario.findByUsuEmail( jsonUsuEnd.getUsuario().getUsuEmail() )
                    .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));

            // Verifique se o endereco existe no banco de dados
            Endereco endereco = buscarEndereco.findById(novoEndereco.getEndId())
                    .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

            // Crie o vínculo entre o usuario e o endereco
            UsuarioEndereco novoVinculo = new UsuarioEndereco(usuario, endereco);
            vincularUsuEnd.save(novoVinculo);

            return ResponseEntity.status(HttpStatus.CREATED).body( endereco );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vínculo: " + e.getMessage());
        }
    }

    @PostMapping("/cadastroCarona")
    public ResponseEntity<?> cadastrarCarona(@Valid @RequestBody Carona carona, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        
        Carona novoCarona = criarCarona.save( carona );

        return ResponseEntity.status(HttpStatus.CREATED).body(novoCarona);
    }

    @PostMapping("/estatisticasComoPassageiro")
    public ResponseEntity<?> getEstatisticasPassageiro(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());
        Passageiro passageiroEncontrado = identificarPassageiro.findByUsuario(usuarioEncontrado);

        if( passageiroEncontrado == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de passageiro nao foram encontrados!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body( passageiroEncontrado );
    }

    @PostMapping("/estatisticasComoMotorista")
    public ResponseEntity<?> getEstatisticasMotorista(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());
        Motorista motoristaEncontrado = identificarMotorista.findByUsuario(usuarioEncontrado);

        if( motoristaEncontrado == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de motorista nao foram encontrados!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body( motoristaEncontrado );
    }

    @GetMapping("/listaDeModelos")
    public Iterable<Modelo> getListaModelos(){
        return modelo.findAll();
    }

    @GetMapping("/listaDeMarcas")
    public Iterable<Marca> getListaMarcas(){
        return marca.findAll();
    }

    @PostMapping("/listaDeVeiculos")
    public List<Veiculo> getListaVeiculos(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());
        Motorista motoristaEncontrado = identificarMotorista.findByUsuario(usuarioEncontrado);

        return identificarVeiculos.findVeiculosDoMotorista(motoristaEncontrado.getMotId());
    }

    @PostMapping("/listaDeEnderecos")
    public List<Endereco> getListaEnderecos(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());

        return identificarEnderecos.findEnderecosDoUsuario(usuarioEncontrado.getUsuId());
    }

    @PostMapping("/historicoDeCaronas")
    public List<Carona> getMeuHistoricoDeCaronas(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmailAndUsuSenha(usuario.getUsuEmail(), usuario.getUsuSenha());
        Passageiro passageiroEncontrado = identificarPassageiro.findByUsuario(usuarioEncontrado);
        Motorista motoristaEncontrado = identificarMotorista.findByUsuario(usuarioEncontrado);

        List<Carona> caronasComoMotorista = criarCarona.findCaronasComoMotorista(
            motoristaEncontrado.getMotId()
        );

        List<Carona> caronasComoPassageiro = identificarCarona.findCaronasComoPassageiro(
            passageiroEncontrado.getPasId()
        );

        List<Carona> todasAsCaronas = new ArrayList<>();
        todasAsCaronas.addAll(caronasComoMotorista);
        todasAsCaronas.addAll(caronasComoPassageiro);

        return todasAsCaronas;
    }

    // Exeplos (Apagar depois)
    @PutMapping("/")
    public Usuario editar( @RequestBody Usuario u ){
        return acao.save(u);
    }

    @DeleteMapping("/{usu_id}")
    public void remover( @PathVariable Integer usu_id ){
        acao.deleteById( usu_id );
    }
}
