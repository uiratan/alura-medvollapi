package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Component
public class ValidadorMotivoDoCancelamento implements ValidadorCancelamentoDeConsulta {

    @Override
    public void validar(DadosCancelamentoConsulta dados) {
        System.out.println("ValidadorMotivoDoCancelamento");
        try {
            System.out.println("ValidadorMotivoDoCancelamento try");
            MotivoCancelamento.valueOf(dados.motivoCancelamento().name());
        } catch (Exception ex) {
            System.out.println("ValidacaoException");
            throw new ValidacaoException("Motivo do cancelamento informado deve ser um motivo válido!");
        }

    }
}
