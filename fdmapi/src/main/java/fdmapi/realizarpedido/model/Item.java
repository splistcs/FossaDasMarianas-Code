package fdmapi.realizarpedido.model;

/* Java import */
import java.math.BigDecimal;
import java.util.Map;

/* JPA import */
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/* FossaDasMarinanas fdmapi import */
import fdmapi.produto.model.Sku;

/* Misc. import */
import jakarta.persistence.CollectionTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Item
 * Mapeado conforme os Astah, o objetivo é realizar um recorte
 * do Sku - "snapshot" - de seus valores para compor o pedido.
 *
 * @see fdmapi.produto.model.Sku
 * @author Magocho
 */

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quantidade")
  private Integer quantidade;

  @Column(name = "preco_final")
  private BigDecimal precoFinal;

  @ManyToOne
  @JoinColumn(name = "sku_id")
  private Sku sku;
}
