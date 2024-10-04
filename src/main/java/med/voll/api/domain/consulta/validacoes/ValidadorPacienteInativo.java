package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorPacienteInativo implements ValidadorAgendamentoConsulta {
    @Autowired
    private PacienteRepository repository;
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if(dadosAgendamentoConsulta.idMedico() == null) {
            return;
        }

        var dadosPaciente =  repository.findAtivoById(dadosAgendamentoConsulta.idPaciente());
        if(!dadosPaciente.getAtivo()) {
            throw new ValidacaoException("O paciente não está ativo no sistema");
        }

    }
}
