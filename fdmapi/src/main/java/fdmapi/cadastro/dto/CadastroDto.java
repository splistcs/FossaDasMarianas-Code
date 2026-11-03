package fdmapi.cadastro.dto;

import fdmapi.cadastro.model.Cadastro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.ArrayList;

import fdmapi.cadastro.model.TipoCadastro;
import fdmapi.endereco.model.Endereco;
import fdmapi.endereco.dto.EnderecoDto;
import fdmapi.realizarpedido.model.Pedido;
import fdmapi.realizarpedido.dto.PedidoDto;

/**
 * DTO de ENTRADA do Manter Cadastro (UC03).
 * Respostas usam CadastroRespostaDto, que não contém a senha.
 */
public record CadastroDto (
    Long id,

    @NotBlank(message = "Nome Inválido") String nomeCompleto,

    @NotBlank(message = "Email Inválido") String email,

    @NotBlank(message = "CPF Inválido") String CPF,

    @NotBlank(message = "Número de telefone inválido") String telefone,

    @NotNull(message = "Estado de atuação não está informado") Boolean ativo,

    @NotBlank(message = "Senha inválida") String senha,
    
    @NotNull(message = "Tipo de cadastro não está informado") Cadastro.TipoCadastro TipoCadastro,

    List<PedidoDto> pedidoDtos,

    List<EnderecoDto> enderecoDtos
) {
    @Override
    public String toString() {
        return "CadastroDto[id=" + id + ", nomeCompleto=" + nomeCompleto + ", email=" + email
            + ", CPF=" + CPF + ", telefone=" + telefone + ", ativo=" + ativo
            + ", senha=*****, TipoCadastro=" + TipoCadastro + "]";
    }  

    public static CadastroDto fromSemPedidoSemEndereco (Cadastro c) {
      return new CadastroDto(c.getId(), c.getNomeCompleto(), c.getEmail(), 
        c.getCpf(), c.getTelefone(), c.isAtivo(), c.getSenha(), 
        c.getTipoCadastro(), null, null);
    }

    // TODO tratar quando EnderecoDto e PedidoDto forem null
    public static CadastroDto from (Cadastro c) {
      List<PedidoDto> pedidoDtos = new ArrayList<>();
      List<EnderecoDto> enderecoDtos = new ArrayList<>();

      for(Pedido p : c.getPedidos()) {
        pedidoDtos.add(PedidoDto.from(p));
      }

      for(Endereco e : c.getEnderecos()) {
        enderecoDtos.add(EnderecoDto.from(e));
      }

      return new CadastroDto(c.getId(), c.getNomeCompleto(), c.getEmail(), 
          c.getCpf(), c.getTelefone(), c.isAtivo(), c.getSenha(), 
          c.getTipoCadastro(), pedidoDtos, enderecoDtos);
    }
}
