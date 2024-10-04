package med.voll.api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Endereco {
    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(DadosEndereco endereco) {
        this.logradouro = endereco.logradouro();
        this.bairro = endereco.bairro();
        this.cep = endereco.cep();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.cidade = endereco.cidade();
        this.uf = endereco.uf();
    }

    public void atualizarInformacoes(DadosEndereco dados) {
        this.logradouro = dados.logradouro() != null ? dados.logradouro() : getLogradouro();
        this.bairro = dados.bairro() != null ? dados.bairro() : getBairro();
        this.cep = dados.cep() != null ? dados.cep() : getCep();
        this.numero = dados.numero() != null ? dados.numero() : getNumero();
        this.complemento = dados.complemento() != null ? dados.complemento() : getComplemento();
        this.cidade = dados.cidade() != null ? dados.cidade() : getCidade();
        this.uf = dados.uf() != null ? dados.uf() : getUf();
    }
}
