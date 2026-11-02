package fdmapi.mantercadastro.service;

/* Java import */
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/* Spring import*/
import org.springframework.stereotype.Service;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Endereco;
import fdmapi.mantercadastro.model.Cliente;
import fdmapi.mantercadastro.model.TipoCadastro;
import fdmapi.mantercadastro.dto.EnderecoDto;
import fdmapi.mantercadastro.dto.ClienteDto;
import fdmapi.mantercadastro.repository.ClienteRepository;
import fdmapi.mantercadastro.repository.EnderecoRepository;

/* Misc. import */
import lombok.extern.log4j.Log4j2;

@Service
public class ManterCadastroService {
  private final ClienteRepository clienteRepository;
  private final EnderecoRepository enderecoRepository;

	public ManterCadastroService(ClienteRepository clienteRepository,
      EnderecoRepository enderecoRepository) {

		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
	}	

  public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

  public Boolean confirmaClientePorId(Long id) {
    Boolean ativo = new Boolean(clienteRepository.selectClienteGetAtivo(id));
    return ativo;
  }

  public ClienteDto findClienteDtoById(Long id) {
		return ClienteDto.from(clienteRepository.findById(id).orElse(null));
	}  

  public EnderecoDto findEnderecoDtoById(Long id) {
		return EnderecoDto.from(enderecoRepository.findById(id).orElse(null));
	}  

  public Optional<Cliente> findClienteById(Long id) {
		return clienteRepository.findById(id);
	}  

  public Optional<Endereco> findEnderecoById(Long id) {
		return enderecoRepository.findById(id);
	}
}
