package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Materiais;

public interface MateriaisRepository extends JpaRepository<Materiais, Long> {

	List<Materiais> findAllByOrderByNomeAsc();

	boolean existsByCategoria_Id(Long categoriaId);

	Optional<Materiais> findByNomeIgnoreCase(String nome);
}
