package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable pageable);

    @Query("""
            SELECT m from Medico m
            WHERE m.ativo = 1 and
                  m.especialidade = :especialidade and
                  m.id not in (SELECT c.medico.id 
                               FROM Consulta c
                               WHERE c.data = :data and
                                     c.motivoCancelamento is null)
            ORDER BY rand()
            LIMIT 1 
            """)
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

    @Query("""
            select m.ativo 
            from Medico m
            where m.id = :idMedico
            """)
    Boolean findAtivoByID(Long idMedico);
}
