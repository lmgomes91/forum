package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
}