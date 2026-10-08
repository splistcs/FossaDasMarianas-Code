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

  public Optional<ClienteDto> findClienteById(Long id) {
		return CLienteDto.from(clienteRepository.findById(id));
	}  

  public Optional<EnderecoDto> findEnderecoById(Long id) {
		return Endereco.from(enderecoRepository.findById(id));
	}
}
