package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

        // devolver 201
        // devolver o cabeçalho location com a uri
        // devolver no corpo da resposta uma representação do recurso recem criado
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(sort={"nome"}) Pageable pageable) {
        //return repository.findAll().stream().map(DadosListagemMedico::new).toList();
        // busca todos os medicos
        //return repository.findAll(pageable).map(DadosListagemMedico::new);

        // busca apenas os medicos com ativo = true usando padrao do spring - by campo valor
        var page = repository.findAllByAtivoTrue(pageable).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);

        // retorna 200
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        // Hard delete  repository.deleteById(id);
        // Exclusao lógica
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));

        //retorna 200
    }

    @PutMapping
    /*
    TODO: Atualizar este método para passar o parâmetro no path e nao no body
     */
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosMedicoAtualizacao dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));

        //retorna 200
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // Hard delete  repository.deleteById(id);
        // Exclusao lógica
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build();
        // retorna 204
    }
}
