package fdmapi.mantercadastro.model;

/* Java import */
import java.util.ArrayList;
import java.util.List;

/* JPA import */
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/* FossaDasMarinanas fdmapi import */
import fdmapi.mantercadastro.model.Cliente;
import fdmapi.realizarpedido.model.Pedido;

/* Misc. import */
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Endereco {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column("cep")
  private String cep;

  @Column("numero")
  private String numero;

  @Column("complemento")
  private String complemento;

  @Column("bairro")
  private String bairro;

  @Column("rua")
  private String rua;

  @Column("cidade")
  private String cidade;
}
