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


//    public Page<DadosListagemMedico> listar(@PageableDefault(size=10, page=0, sort={"nome"}) Pageable pageable) {

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(sort={"nome"}) Pageable pageable) {
        //return repository.findAll().stream().map(DadosListagemMedico::new).toList();
        // busca todos os medicos
        //return repository.findAll(pageable).map(DadosListagemMedico::new);

        // busca apenas os medicos com ativo = true usando padrao do spring - by campo valor
        return repository.findAllByAtivoTrue(pageable).map(DadosListagemMedico::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosMedicoAtualizacao dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }
    @DeleteMapping("{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        // Hard delete  repository.deleteById(id);
        // Exclusao lógica
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }
}
