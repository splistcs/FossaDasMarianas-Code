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

  @Column(name ="cep")
  private String cep;

  @Column(name = "numero")
  private String numero;

  @Column(name = "complemento")
  private String complemento;

  @Column(name = "bairro")
  private String bairro;

  @Column(name = "rua")
  private String rua;

  @Column(name = "cidade")
  private String cidade;
}
