package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.*;
import med.voll.api.domain.endereco.Endereco;
@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode(of = "id")
public class Medico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @Embedded //quando é usado uma classe complementar que não possui uma tabela no banco de dados, utilizamos a anotação Embedded do JPA para sinalizar isso
    private Endereco endereco;
    private Boolean ativo;

    public Medico(DadosCadastroMedico dadosCadastroMedico) {
        this.nome = dadosCadastroMedico.nome();
        this.email = dadosCadastroMedico.email();
        this.telefone = dadosCadastroMedico.telefone();
        this.crm = dadosCadastroMedico.crm();
        this.especialidade = dadosCadastroMedico.especialidade();
        this.endereco = new Endereco(dadosCadastroMedico.endereco());
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizaMedico dados) {
        this.nome = dados.nome() != null ? dados.nome() : getNome();
        this.telefone = dados.telefone() != null ? dados.telefone() : getTelefone();
        if (dados.endereco() != null)
            this.endereco.atualizarInformacoes(dados.endereco());

    }

    public void excluir() {
        this.ativo = false;
    }
}
