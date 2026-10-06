package fdmapi.realizarpedido.model;

/* Java import */
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/* JPA import */
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

/* FossaDasMarinanas fdmapi import */
import fdmapi.realizarpedido.model.StatusPedido;
import fdmapi.realizarpedido.model.Item;
// import fdmapi.<----->.Endereco;

/* Misc. import */
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Pedido {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data_pedido")
  private LocalDateTime dataPedido;

  @Column(name = "valor_total")
  private BigDecimal valorTotal;

  @Column(name = "valor_frete")
  private BigDecimal valorFrete;

  @Column(name = "id_sessao")
  private Integer idSessao;

  @Column(name = "status")
  @Enumerated(EnumType.ORDINAL)
  private StatusPedido statusPedido;

  /* A implementação de endereco vai depender do cadastro. */
  // @ManyToOne
  // @JoinColumn(name = "endereco_id")
  // private Endereco endereco;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(name = "pedido_item", 
             joinColumns = @JoinColumn(name = "pedido_id"), 
             inverseJoinColumns = @JoinColumn(name = "item_id"))
  private List<Item> itemPedido;
}
