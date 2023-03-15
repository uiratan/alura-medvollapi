package med.voll.api.domain.consulta;

import lombok.ToString;

@ToString
public enum MotivoCancelamento {
    PACIENTE_DESISTIU,
    MEDICO_CANCELOU,
    OUTROS;

}
