/*package fdmapi.mantercadastro.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinTable;

import fdmapi.mantercadastro.model.Endereco;
import fdmapi.mantercadastro.model.TipoCadastro;
import fdmapi.realizarpedido.model.Pedido;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_completo")
  private String nomeCompleto;

  @Column(name = "email")
  private String email;

  @Column(name = "cpf")
  private String cpf;

  @Column(name = "telefone")
  private String telefone;

  @Column(name = "ativo")
  private Boolean ativo;

  @Column(name = "senha")
  private String senha;

  @Column(name = "tipo_cadastro")
  @Enumerated(EnumType.ORDINAL)
  private TipoCadastro tipoCadastro;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(name = "cliente_pedido", 
             joinColumns = @JoinColumn(name = "cliente_id"), 
             inverseJoinColumns = @JoinColumn(name = "pedido_id"))
  private List<Pedido> pedidos;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(name = "cliente_endereco", 
             joinColumns = @JoinColumn(name = "cliente_id"), 
             inverseJoinColumns = @JoinColumn(name = "endereco_id"))
  private List<Endereco> enderecos;
}*/
