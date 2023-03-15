package med.voll.api.domain.consulta;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,
        @NotNull
        MotivoCancelamento motivoCancelamento

) {
}
