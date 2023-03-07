package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(

        //@NotNull
        @NotBlank // n pode ser nulo e vazio
        String nome,
        @NotBlank // n pode ser nulo e vazio
        @Email
        String email,

        @NotBlank // n pode ser nulo e vazio
        String telefone,

        @NotBlank // n pode ser nulo e vazio
        @Pattern(regexp = "\\d{4,6}")
        String crm,

        @NotNull
        Especialidade especialidade,

       @NotNull
       @Valid
        DadosEndereco endereco) {

}
