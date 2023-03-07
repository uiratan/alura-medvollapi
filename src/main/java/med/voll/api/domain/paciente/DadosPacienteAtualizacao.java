package med.voll.api.domain.paciente;

import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosPacienteAtualizacao(

        String nome,
        String email,
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}") String cpf,
        String telefone,
        DadosEndereco endereco
) {
}
