package med.voll.api.controller;

import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    public void cadastrar(@RequestBody DadosCadastroMedico dados) {

        repository.save(new Medico(dados));
    }

    @GetMapping
    public void listar() {
        System.out.println(repository.findAll().toString());
    }


}
