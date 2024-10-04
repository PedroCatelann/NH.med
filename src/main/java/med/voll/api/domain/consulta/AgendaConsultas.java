package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoConsulta> validadores;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if(!pacienteRepository.existsById(dadosAgendamentoConsulta.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe!");
        }
        if(dadosAgendamentoConsulta.idMedico() != null && !medicoRepository.existsById(dadosAgendamentoConsulta.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        validadores.forEach(v -> v.validar(dadosAgendamentoConsulta));

        var paciente = pacienteRepository.getReferenceById(dadosAgendamentoConsulta.idPaciente());
        var medico = escolherMedico(dadosAgendamentoConsulta);
        if(medico == null)
            throw new ValidacaoException("Não existe nenhum médico disponível nessa data");

        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data(), null);

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }



    private Medico escolherMedico(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if(dadosAgendamentoConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(dadosAgendamentoConsulta.idMedico());
        }

        if(dadosAgendamentoConsulta.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatório quando não haver um médico escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dadosAgendamentoConsulta.especialidade(), dadosAgendamentoConsulta.data());
    }

    public void cancelarConsulta(DadosCancelamentoConsulta dados) {
        var consulta = consultaRepository.getReferenceById(dados.id());
        if (!consultaRepository.existsById(dados.id())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        if(dados.motivo() == null) {
            throw new ValidacaoException("É necessário informar um motivo para o cancelamento");
        }

        validaDataAntecedencia24(dados, consulta);
        consulta.cancelar(dados.motivo());
    }

    public void validaDataAntecedencia24(DadosCancelamentoConsulta dados, Consulta consulta) {

        var data_agora = LocalDateTime.now();

        var data_cancelamento = Duration.between(data_agora, consulta.getData()).toHours();

        if(data_cancelamento < 24) {
            throw new ValidacaoException("A consulta só poderá ser desmarcada com antecedência de 24 horas");
        }
    }
}
