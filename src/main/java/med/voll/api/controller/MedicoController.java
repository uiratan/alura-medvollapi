package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    @GetMapping
//    public Page<DadosListagemMedico> listar(@PageableDefault(size=10, page=0, sort={"nome"}) Pageable pageable) {
    public Page<DadosListagemMedico> listar(@PageableDefault(sort={"nome"}) Pageable pageable) {
        //return repository.findAll().stream().map(DadosListagemMedico::new).toList();
        return repository.findAll(pageable).map(DadosListagemMedico::new);
    }
}
