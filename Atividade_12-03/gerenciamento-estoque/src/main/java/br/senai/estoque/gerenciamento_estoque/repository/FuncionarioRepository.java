package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	long countByAtivoTrue();

	Optional<Funcionario> findByNif(String nif);

	Optional<Funcionario> findByNifAndAtivoTrue(String nif);

	boolean existsByNif(String nif);
}
