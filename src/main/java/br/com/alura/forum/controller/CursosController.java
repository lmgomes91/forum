package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.CursoDto;
import br.com.alura.forum.controller.form.CursoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class CursosController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeCursos")
    public Page<CursoDto> lista (@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id",
                                      direction = Sort.Direction.DESC,
                                      page = 0, size = 5) Pageable paginacao){
        Page<Curso> cursos;
        if (nomeCurso == null){
            cursos = cursoRepository.findAll(paginacao);
        } else {
            cursos = cursoRepository.findByNome(nomeCurso, paginacao);
        }

        return CursoDto.converter(cursos);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<CursoDto> cadastrar(@RequestBody @Valid CursoForm form, UriComponentsBuilder uriComponentsBuilder){
       Curso curso = form.converter();
       cursoRepository.save(curso);

       URI uri = uriComponentsBuilder.path("cursos/{id}").buildAndExpand(curso.getId()).toUri();

       return ResponseEntity.created(uri).body(new CursoDto(curso));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> detalhar(@PathVariable Long id){
        Optional<Curso> curso = cursoRepository.findById(id);
        if(curso.isPresent()){
            return ResponseEntity.ok(curso.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<Boolean> remover(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if(curso.isPresent()){
            cursoRepository.deleteById(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<CursoDto> atualizar(@PathVariable Long id, @RequestBody @Valid CursoForm form){
        Optional<Curso> curso = cursoRepository.findById(id);

        if(curso.isPresent()){
            Curso cursoAtualizado = form.atualizar(id, cursoRepository);
            return ResponseEntity.ok(new CursoDto(cursoAtualizado));
        }

        return ResponseEntity.notFound().build();
    }


}
