package fdmapi.cadastro.model;

import java.util.ArrayList;
import java.util.List;

import fdmapi.endereco.model.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cadastro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    private boolean ativo;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCadastro tipoCadastro;

    /*
     * Composição Cadastro -> Endereco (1..*), unidirecional como no diagrama.
     * - cascade ALL: salvar/excluir o cadastro salva/exclui seus endereços.
     * - orphanRemoval: endereço retirado da lista é excluído do banco.
     * - @JoinColumn(nullable = false): a chave estrangeira cadastro_id fica na
     *   tabela endereco e é gravada já no INSERT do endereço.
     * - @Builder.Default: sem isso, o builder do Lombok criaria a lista nula.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cadastro_id", nullable = false)
    @Builder.Default
    private List<Endereco> enderecos = new ArrayList<>();

    public enum TipoCadastro {
        ADMIN,
        CLIENTE
    }
}
