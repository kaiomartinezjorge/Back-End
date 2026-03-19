package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	List<Categoria> findAllByOrderByNomeAsc();

	Optional<Categoria> findByNomeIgnoreCase(String nome);

	boolean existsByNomeIgnoreCase(String nome);
}
