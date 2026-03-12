package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Materiais;

public interface MateriaisRepository extends JpaRepository<Materiais, Long> {

	List<Materiais> findAllByOrderByNomeAsc();

	boolean existsByCategoriaId(Long categoriaId);
}
