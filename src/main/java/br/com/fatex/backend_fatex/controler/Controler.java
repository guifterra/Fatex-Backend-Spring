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

import org.jasypt.util.text.BasicTextEncryptor;

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

        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());

        if ((usuarioEncontrado) != null) {

            if( descriptografar(usuarioEncontrado.getUsuSenha()).equalsIgnoreCase(usuario.getUsuSenha()) )
                return ResponseEntity.ok(usuarioEncontrado);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email incorreto");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        // Criando e salvando Usuario no banco
        usuario.setUsuSenha( criptografar( usuario.getUsuSenha() ) );
        Usuario novoUsuario = acao.save( usuario );

        // Criando e salvando a conta como Passageiro no banco
        criarContaPassageiro( novoUsuario );

        // Testando se há necessicade de criar conta Motorista
        if( novoUsuario.getUsuTipo().name().equalsIgnoreCase("MOTORISTA") ){
            criarContaMotorista( novoUsuario );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    public static String criptografar(String texto) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("chave-projeto-fatex");
        return textEncryptor.encrypt(texto);
    }

    public static String descriptografar(String texto) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("chave-projeto-fatex");
        return textEncryptor.decrypt(texto);
    }

    @PutMapping("/editarDadosDoPerfil")
    public ResponseEntity<?> editarDadosDoPerfil(@Valid @RequestBody Usuario usuario, BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        // Atualizando e salvando Usuario no banco
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());

        usuarioEncontrado.setUsuDtNascimento(usuario.getUsuDtNascimento());
        usuarioEncontrado.setUsuCpf(usuario.getUsuCpf());
        usuarioEncontrado.setUsuGenero(usuario.getUsuGenero());
        usuarioEncontrado.setUsuNome(usuario.getUsuNome());
        usuarioEncontrado.setUsuTelefone(usuario.getUsuTelefone());

        return ResponseEntity.status(HttpStatus.CREATED).body(acao.save( usuarioEncontrado ));
    }

    public void criarContaPassageiro( Usuario usuario ){

        Passageiro passageiro = new Passageiro( usuario );
        criarPassageiro.save( passageiro );
    }

    public void criarContaMotorista( Usuario usuario ){

        Motorista novoMotorista = new Motorista( usuario );
        criarMotorista.save( novoMotorista );
    }

    @PostMapping("/tornarMeMotorista")
    public ResponseEntity<?> tornarMeMotorista(@Valid @RequestBody Usuario usuario, BindingResult result){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());

        if( usuarioEncontrado != null ){

            Motorista motoristaEncontrado = identificarMotorista.findByUsuario(usuarioEncontrado);

            if( motoristaEncontrado == null ){

                usuarioEncontrado.setUsuTipo( TipoUsuario.valueOf("MOTORISTA") );
                editarDadosDoPerfil( usuarioEncontrado, result );

                criarContaMotorista( usuarioEncontrado );

                return ResponseEntity.status(HttpStatus.CREATED).body("Conta de motorista criada com sucesso!");

            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vc ja possui uma conta de motorista!");
            }

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de usuario nao foram encontrados!");
        }
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

    @PutMapping("/editarVeiculo")
    public ResponseEntity<?> editarVeiculo(@Valid @RequestBody Veiculo veiculo, BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Veiculo veiculoEncontrado = buscarVeiculo.findByVeiPlaca(veiculo.getVeiPlaca()).orElse(null);

        if( veiculoEncontrado != null ){
            
            veiculoEncontrado.setVeiPlaca( veiculo.getVeiPlaca() );
            veiculoEncontrado.setVeiMaxPassageiros( veiculo.getVeiMaxPassageiros() );
            veiculoEncontrado.setVeiCor( veiculo.getVeiCor() );
            veiculoEncontrado.setMarca( veiculo.getMarca() );
            veiculoEncontrado.setModelo( veiculo.getModelo() );

            criarVeiculo.save( veiculoEncontrado );
            return ResponseEntity.status(HttpStatus.CREATED).body("Veiculo atualizado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O veiculo nao foi encontrado!");
    }

    @PutMapping("/alterarVisibilidadeDoMeuVeiculo")
	public ResponseEntity<?> alterarVisibilidadeDoVeiculo(@Valid @RequestBody JsonMotVei jsonMotVei, BindingResult result) {

		try {
			// Verifique se o veículo existe no banco de dados
			Veiculo veiculo = buscarVeiculo.findByVeiPlaca(jsonMotVei.getVeiculo().getVeiPlaca())
					.orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

			// Verifique se o motorista existe no banco de dados
			Motorista motorista = criarMotorista.findById(jsonMotVei.getMotorista().getMotId())
					.orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado"));

			// Crie o vínculo entre o motorista e o veículo
			MotoristaVeiculo alterarVisibilidade = vincularMotVei.findByVeiculoAndMotorista( veiculo, motorista );
            
            if( alterarVisibilidade.getMhvStatus().toString().equalsIgnoreCase("ATIVO") ){

                alterarVisibilidade.setMhvStatus( Visibilidade.INATIVO );
            }else{
                alterarVisibilidade.setMhvStatus( Visibilidade.ATIVO );
            }

			vincularMotVei.save(alterarVisibilidade);

			return ResponseEntity.status(HttpStatus.CREATED).body("Visibilidade alterada com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao Alterar visibilidade: " + e.getMessage());
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

                Endereco enderecoEncontrado = criarEndereco.findEndereco(latitude, longitude);

                if( enderecoEncontrado == null ){
                    Endereco novoEndereco = criarEndereco.save(jsonUsuEnd.getEndereco());
                    return vincularUsuarioAoEndereco(jsonUsuEnd, novoEndereco);
                }else{
                    return vincularUsuarioAoEndereco(jsonUsuEnd, enderecoEncontrado);
                }

            } catch (NumberFormatException e) {
                // Erro no formato da coordenada
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de coordenadas inválido.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados geográficos inválidos.");
        }
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

    @PutMapping("/alterarVisibilidadeDoMeuEndereco")
	public ResponseEntity<?> alterarVisibilidadeDoMeuEndereco(@Valid @RequestBody JsonUsuEnd jsonUsuEnd ) {

		try {
            // Verifique se o usuario existe no banco de dados
            Usuario usuario = buscarUsuario.findByUsuEmail( jsonUsuEnd.getUsuario().getUsuEmail() )
                    .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));

            // Verifique se o endereco existe no banco de dados
            Endereco endereco = buscarEndereco.findById(jsonUsuEnd.getEndereco().getEndId())
                    .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

            // Crie o vínculo entre o usuario e o endereco
            UsuarioEndereco alterarVisibilidade = vincularUsuEnd.findByUsuarioAndEndereco(usuario, endereco);
            
            if( alterarVisibilidade.getUheStatus().toString().equalsIgnoreCase("ATIVO") ){

                alterarVisibilidade.setUheStatus( Visibilidade.INATIVO );
            }else{
                alterarVisibilidade.setUheStatus( Visibilidade.ATIVO );
            }

            vincularUsuEnd.save(alterarVisibilidade);

            return ResponseEntity.status(HttpStatus.CREATED).body("Visibilidade alterada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao Alterar visibilidade: " + e.getMessage());
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

    // Para entrada e saida de motorista
    @PutMapping("/atualizarCarona")
    public ResponseEntity<?> atualizarCarona(@Valid @RequestBody Carona carona, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        
        Carona caronaEncontrada = criarCarona.findCarona( carona.getCarId() );

        if( caronaEncontrada.getMotoristaVeiculo() == null ){

            MotoristaVeiculo motoristaVeiculo = identificarVeiculos.findMotVei( carona.getMotoristaVeiculo().getMotorista().getMotId(), carona.getMotoristaVeiculo().getVeiculo().getVeiId());
            caronaEncontrada.setMotoristaVeiculo( motoristaVeiculo );
        
        }else{

            if( caronaEncontrada.getMotoristaVeiculo().getMotorista().getMotId() == carona.getMotoristaVeiculo().getMotorista().getMotId() ){
                
                if( caronaEncontrada.getMotoristaVeiculo().getVeiculo().getVeiId() == carona.getMotoristaVeiculo().getVeiculo().getVeiId() ){
                    
                    caronaEncontrada.setMotoristaVeiculo( null );
                    criarCarona.save( caronaEncontrada );
                    return ResponseEntity.status(HttpStatus.CREATED).body("Voce saiu da carona com sucesso!");

                }else{

                    MotoristaVeiculo motoristaVeiculo = identificarVeiculos.findMotVei( carona.getMotoristaVeiculo().getMotorista().getMotId(), carona.getMotoristaVeiculo().getVeiculo().getVeiId());
                    caronaEncontrada.setMotoristaVeiculo( motoristaVeiculo );
                    criarCarona.save( caronaEncontrada );
                    return ResponseEntity.status(HttpStatus.CREATED).body("Veiculo atualizado com sucesso!");
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Essa carona ja possui um motorista!");    
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body( criarCarona.save( caronaEncontrada ) );
    }

    @PostMapping("/entrarNaCarona")
    public ResponseEntity<?> entrarNaCarona(@Valid @RequestBody JsonPasCar jsonPasCar, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        
        if( identificarCarona.identificarVagasOcupadas(jsonPasCar.getCarona().getCarId()) < jsonPasCar.getCarona().getCarVagas() ){
            PassageiroCarona entradaNaCarona = new PassageiroCarona( jsonPasCar.getPassageiro(), jsonPasCar.getCarona() );
            identificarCarona.save( entradaNaCarona );
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Entrada na carona realizada com sucesso!");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A carona esta lotada, nao eh possivel entrar!");
        }
    }

    @PutMapping("/avaliarPassageiro")
    public ResponseEntity<?> avaliarPassageiro(@RequestBody Passageiro passageiro){
    
        Passageiro passageiroEncontrado = identificarPassageiro.findByUsuario( passageiro.getUsuario() );

        if( passageiroEncontrado == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de passageiro nao foram encontrados!");
        }

        if( passageiro.getPasNota().doubleValue() >= 0 && passageiro.getPasNota().doubleValue() <= 5.0 ){

            passageiroEncontrado.setPasQtAvaliacoes( passageiroEncontrado.getPasQtAvaliacoes() + 1 );
            passageiroEncontrado.setPasNota( ( passageiroEncontrado.getPasNota().add( passageiro.getPasNota() ) ) );

            criarPassageiro.save( passageiroEncontrado );

            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliacao realizada com sucesso!");

        }else{

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor da nota nao esta no limite de 0 a 5!");
        }
    }

    @PutMapping("/avaliarMotorista")
    public ResponseEntity<?> avaliarMotorista(@RequestBody Motorista motorista){
    
        Motorista motoristaEncontrado = identificarMotorista.findByUsuario( motorista.getUsuario() );

        if( motoristaEncontrado == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de motorista nao foram encontrados!");
        }

        if( motorista.getMotNota().doubleValue() >= 0.0 && motorista.getMotNota().doubleValue() <= 5.0 ){
            
            motoristaEncontrado.setMotQtAvaliacoes( motoristaEncontrado.getMotQtAvaliacoes() + 1 );
            motoristaEncontrado.setMotNota( ( motoristaEncontrado.getMotNota().add( motorista.getMotNota() ) ) );

            criarMotorista.save( motoristaEncontrado );

            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliacao realizada com sucesso!");

        }else{

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor da nota nao esta no limite de 0 a 5!");
        }
        
    }

    public String getCoordinates(String address) {
        return geocodingService.getCoordinates(address);
    }

    public String getAddress(double lat, double lng) {
        return reverseGeocodingService.getAddress(lat, lng);
    }

    @PostMapping("/estatisticasComoPassageiro")
    public ResponseEntity<?> getEstatisticasPassageiro(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());
        Passageiro passageiroEncontrado = identificarPassageiro.findByUsuario(usuarioEncontrado);

        if( passageiroEncontrado == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seus dados de passageiro nao foram encontrados!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body( passageiroEncontrado );
    }

    @PostMapping("/estatisticasComoMotorista")
    public ResponseEntity<?> getEstatisticasMotorista(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());
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

    @GetMapping("/listaDeCaronasSolicitadas")
    public List<Carona> getListaCaronasSolicitas(){
        return criarCarona.findCaronasSolicitadas();
    }

    @GetMapping("/listaDeCaronasFornecidas")
    public List<Carona> getListaCaronasFornecidas(){
        
        List<Carona> todasAsCaronasOfertadas = criarCarona.findOferecerCarona();
        List<Carona> caronasOfertadasFiltradas = new ArrayList<Carona>();

        for (Carona carona : todasAsCaronasOfertadas){

            if(identificarCarona.identificarVagasOcupadas(carona.getCarId()) < carona.getCarVagas()){
               caronasOfertadasFiltradas.add(carona);
            }
        }
        return caronasOfertadasFiltradas;
    }

    @PostMapping("/listaDeVeiculos")
    public List<Veiculo> getListaVeiculos(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());
        Motorista motoristaEncontrado = identificarMotorista.findByUsuario(usuarioEncontrado);

        return identificarVeiculos.findVeiculosDoMotorista(motoristaEncontrado.getMotId());
    }

    @PostMapping("/listaDeEnderecos")
    public List<Endereco> getListaEnderecos(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());

        return identificarEnderecos.findEnderecosDoUsuario(usuarioEncontrado.getUsuId());
    }

    @PostMapping("/historicoDeCaronas")
    public List<Carona> getMeuHistoricoDeCaronas(@RequestBody Usuario usuario){
        
        Usuario usuarioEncontrado = login.findByUsuEmail(usuario.getUsuEmail());
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

    @DeleteMapping("/sairDaCarona")
    public ResponseEntity<?> sairDaCarona(@Valid @RequestBody JsonPasCar jsonPasCar, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        
        Carona caronaEncontrada = criarCarona.findCarona( jsonPasCar.getCarona().getCarId() );

        if( caronaEncontrada != null )
            identificarCarona.removerPassageiroDaCarona(jsonPasCar.getPassageiro().getPasId(), caronaEncontrada.getCarId());
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carona nao foi encontrda!");

        return ResponseEntity.status(HttpStatus.CREATED).body("Sua saida da carona ocorreu com sucesso!");        
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
