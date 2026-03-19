package br.senai.estoque.gerenciamento_estoque;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.FuncionarioAutenticado;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioAutenticadoRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;

@SpringBootApplication
public class GerenciamentoEstoqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciamentoEstoqueApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner carregarDadosIniciais(FuncionarioAutenticadoRepository funcionarioAutenticadoRepository,
			FuncionarioRepository funcionarioRepository, CategoriaRepository categoriaRepository,
			AtivoPatrimonialRepository ativoPatrimonialRepository) {
		return args -> {
			criarFuncionarioAutorizado(funcionarioAutenticadoRepository, "kaio", "123");


			Funcionario funcionarioPadrao = criarFuncionarioPadrao(funcionarioRepository);
			List<Categoria> categorias = criarCategoriasPadrao(categoriaRepository);
			criarAtivosPadrao(ativoPatrimonialRepository, funcionarioPadrao, categorias);
		};
	}

	private static Funcionario criarFuncionarioPadrao(FuncionarioRepository funcionarioRepository) {
		Funcionario funcionario = funcionarioRepository.findByNif("123").orElse(new Funcionario());
		funcionario.setNome("kaio");
		funcionario.setNif("123");
		funcionario.setSenha("1234");
		funcionario.setAtivo(true);
		return funcionarioRepository.save(funcionario);
	}

	private static List<Categoria> criarCategoriasPadrao(CategoriaRepository categoriaRepository) {
		if (categoriaRepository.count() > 0) {
			return categoriaRepository.findAllByOrderByNomeAsc();
		}

		List<Categoria> categorias = new ArrayList<>();

		Categoria informatica = new Categoria();
		informatica.setNome("informatica");
		informatica.setDescricao("itens de informatica e perifericos");
		categorias.add(categoriaRepository.save(informatica));

		Categoria manutencao = new Categoria();
		manutencao.setNome("manutencao");
		manutencao.setDescricao("materiais de apoio e manutencao");
		categorias.add(categoriaRepository.save(manutencao));

		Categoria patrimonio = new Categoria();
		patrimonio.setNome("patrimonio");
		patrimonio.setDescricao("ativos patrimoniais da unidade");
		categorias.add(categoriaRepository.save(patrimonio));

		return categorias;
	}

	private static void criarAtivosPadrao(AtivoPatrimonialRepository ativoPatrimonialRepository,
			Funcionario funcionarioPadrao, List<Categoria> categorias) {
		if (ativoPatrimonialRepository.count() > 0) {
			return;
		}

		Categoria categoriaPatrimonio = categorias.get(2);

		AtivoPatrimonial notebook = new AtivoPatrimonial();
		notebook.setNome("notebook dell latitude");
		notebook.setDescricao("notebook usado no laboratorio 1");
		notebook.setNumeroPatrimonio("PAT-001");
		notebook.setLocalizacao("laboratorio 1");
		notebook.setStatus("em uso");
		notebook.setCategoria(categoriaPatrimonio);
		notebook.setCadastradoPor(funcionarioPadrao);
		ativoPatrimonialRepository.save(notebook);

		AtivoPatrimonial projetor = new AtivoPatrimonial();
		projetor.setNome("projetor epson");
		projetor.setDescricao("projetor da sala de treinamento");
		projetor.setNumeroPatrimonio("PAT-002");
		projetor.setLocalizacao("sala de treinamento");
		projetor.setStatus("em uso");
		projetor.setCategoria(categoriaPatrimonio);
		projetor.setCadastradoPor(funcionarioPadrao);
		ativoPatrimonialRepository.save(projetor);
	}

	private static void criarFuncionarioAutorizado(FuncionarioAutenticadoRepository funcionarioAutenticadoRepository,
			String nome, String nif) {
		FuncionarioAutenticado funcionario = funcionarioAutenticadoRepository.findByNifAndAtivoTrue(nif)
				.orElse(new FuncionarioAutenticado());
		funcionario.setNome(nome);
		funcionario.setNif(nif);
		funcionario.setAtivo(true);
		funcionarioAutenticadoRepository.save(funcionario);
	}
}
