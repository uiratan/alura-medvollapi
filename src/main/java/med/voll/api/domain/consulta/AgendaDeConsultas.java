package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    // injeta todos os validadores através da interface na lista validadores
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {

        // validações de integridade,
        // validar os IDs e lógica para escolher um médico livre aleatório caso ele não venha na requisição
        // se for passado o id de um médico que nao existe no banco
        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        // validação atraves da interface
        // Design Pattern Strategy
        // quase um strategy, pois na teoria usaria-se apenas uma estratégia e aqui estao sendo chamadas todas
        // SOLID
        // S - Single Responsability Principle, através dos validadores
        // O - Open-Closed Principle, no serviço AgendaDeConultas: fechada pra modificação mas aberta para extensão
        //      através dos validadores
        // D - Dependency Inversion Principle - a classe service depende da abstração e nao das implementações
        //      específicas

        // regras de negócio atraves de classes especificas
        // ValidadorHorarioAntecedencia
        // ValidadorHorarioFuncionamentoClinica
        // ValidadorMedicoAtivo
        // ValidadorMedicoComOutraConsultaAgendadaNoMesmoHorario
        // ValidadorPacienteAtivo
        // ValidadorPacienteSemOutraConsultaNoDia
        // As seguintes regras de negócio devem ser validadas pelo sistema
        // O horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00;
        // As consultas tem duração fixa de 1 hora; As consultas devem ser agendadas com antecedência mínima de 30 minutos;
        // Não permitir o agendamento de consultas com pacientes inativos no sistema;
        // Não permitir o agendamento de consultas com médicos inativos no sistema;
        // Não permitir o agendamento de mais de uma consulta no mesmo dia para um mesmo paciente;
        // Não permitir o agendamento de uma consulta com um médico que já possui outra consulta agendada na mesma data/hora;
        // A escolha do médico é opcional, sendo que nesse caso o sistema deve escolher aleatoriamente algum médico disponível na data/hora preenchida.
        validadores.forEach(validador -> validador.validar(dados));

        // o método findById() não devolve a entidade, mas um Optional,
        // assim precisamos escrever .get() ao lado de findById().
        // Isso faz com que ele pegue a entidade carregada.
        // var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        // ao usar o getReferenceById não precisa usar o get()
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Não existe médico disponível nesta data!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        // se chegou o id de um médico aqui ele existe no banco
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        // aqui o medico chegou com id nulo
        // pegar médico aleatorio da especialidade informada, logo é obrigatório ser informada
        // que nao tenha outra consulta neste dia
        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido.");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        // Descrição
        //O sistema deve possuir uma funcionalidade que permita o cancelamento de consultas,
        // na qual as seguintes informações deverão ser preenchidas:
        //As seguintes regras de negócio devem ser validadas pelo sistema:
        //
        //Consulta
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }



        //Motivo do cancelamento
        //É obrigatório informar o motivo do cancelamento da consulta,
        // dentre as opções: paciente desistiu, médico cancelou ou outros;
       // TODO: validar se o motivo passado é válido

        //Uma consulta somente poderá ser cancelada com antecedência mínima de 24 horas.
        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivoCancelamento());
    }
}
