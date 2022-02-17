package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.repository.CursoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CursoForm {

    @NotNull
    @NotEmpty
    @Length(min = 5)
    private String nome;

    @NotNull
    @NotEmpty
    @Length(min = 5)
    private String categoria;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Curso converter(){
        return new Curso(nome, categoria);
    }

    public Curso atualizar(Long id, CursoRepository cursoRepository) {
        Curso curso = cursoRepository.getById(id);

        curso.setCategoria(this.categoria);
        curso.setNome(this.nome);

        return curso;
    }
}
