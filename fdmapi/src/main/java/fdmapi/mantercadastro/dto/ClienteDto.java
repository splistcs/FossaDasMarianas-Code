package fdmapi.mantercadastro.dto;

/* Java import */
import java.util.List;
import java.util.ArrayList;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Cliente;
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.model.PedidoDto;
import fdmapi.mantercadastro.model.TipoCadastro;
import fdmapi.mantercadastro.model.Endereco;
import fdmapi.mantercadastro.dto.EnderecoDto;

public record ClienteDto (Long id, String nomeCompleto, String email, 
    String cpf, String telefone, Boolean ativo, String senha, 
    TipoCadastro tipoCadastro, List<PedidoDto> pedidoDtos, List<Endereco> enderecoDtos) {

  public static ClienteDto fromSemPedidoSemEndereco (Cliente c) {
    return new ClienteDto(c.getId(), c.getNomeCompleto(), c.getEmail(), 
        c.getCpf(), c.getTelefone(), c.getAtivo(), c.getSenha(), 
        c.getTipoCadastro(), null, null);
  }

  public static ClienteDto from (Cliente c) {
    List<PedidoDto> pedidoDtos = new ArrayList<>();
    List<EnderecoDto> enderecoDtos = new ArrayList<>();

    for(Pedido p : c.getPedidos()) {
      pedidoDtos.add(PedidoDto.from(p));
    }

    for(Endereco e : c.getEnderecos()) {
      enderecoDtos.add(EnderecoDto.from(e));
    }

    return new ClienteDto(c.getId(), c.getNomeCompleto(), c.getEmail(), 
        c.getCpf(), c.getTelefone(), c.getAtivo(), c.getSenha(), 
        c.getTipoCadastro(), pedidoDtos, enderecoDtos);
  }
}
