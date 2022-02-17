package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNome(String nomeCurso);

    Page<Curso> findByNome(String nomeCurso, Pageable paginacao);
}