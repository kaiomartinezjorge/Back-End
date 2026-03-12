package br.senai.estoque.gerenciamento_estoque.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.FuncionarioAutenticado;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioAutenticadoRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;

@Component
public class DadosIniciais implements CommandLineRunner {

	@Autowired
	private FuncionarioAutenticadoRepository funcionarioAutenticadoRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@Override
	@Transactional
	public void run(String... args) {
		criarFuncionariosAutorizados();
		Funcionario funcionarioPadrao = criarFuncionarioPadrao();
		List<Categoria> categorias = criarCategoriasPadrao();
		criarMateriaisPadrao(funcionarioPadrao, categorias);
		criarAtivosPadrao(funcionarioPadrao, categorias);
	}

	private void criarFuncionariosAutorizados() {
		criarFuncionarioAutorizado("kaio", "123");
		criarFuncionarioAutorizado("Ana Souza", "1001");
		criarFuncionarioAutorizado("Carlos Lima", "1002");
		criarFuncionarioAutorizado("Marina Alves", "1003");
	}

	private Funcionario criarFuncionarioPadrao() {
		Funcionario funcionario = funcionarioRepository.findByNif("123").orElse(new Funcionario());
		funcionario.setNome("kaio");
		funcionario.setNif("123");
		funcionario.setSenha("1234");
		funcionario.setAtivo(true);
		return funcionarioRepository.save(funcionario);
	}

	private List<Categoria> criarCategoriasPadrao() {
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

	private void criarMateriaisPadrao(Funcionario funcionarioPadrao, List<Categoria> categorias) {
		if (materiaisRepository.count() > 0) {
			return;
		}

		Categoria categoriaInformatica = categorias.getFirst();
		Categoria categoriaManutencao = categorias.get(1);

		Materiais teclado = new Materiais();
		teclado.setNome("teclado usb");
		teclado.setDescricao("teclado padrao para laboratorio");
		teclado.setCategoria(categoriaInformatica);
		teclado.setCriadoPor(funcionarioPadrao);
		teclado.setQuantidade(15);
		teclado = materiaisRepository.save(teclado);

		Materiais mouse = new Materiais();
		mouse.setNome("mouse optico");
		mouse.setDescricao("mouse para uso nas salas de informatica");
		mouse.setCategoria(categoriaInformatica);
		mouse.setCriadoPor(funcionarioPadrao);
		mouse.setQuantidade(20);
		mouse = materiaisRepository.save(mouse);

		Materiais caboRede = new Materiais();
		caboRede.setNome("cabo de rede");
		caboRede.setDescricao("cabo para manutencao de rede interna");
		caboRede.setCategoria(categoriaManutencao);
		caboRede.setCriadoPor(funcionarioPadrao);
		caboRede.setQuantidade(8);
		caboRede = materiaisRepository.save(caboRede);

		salvarMovimentacaoInicial(teclado, funcionarioPadrao, 15);
		salvarMovimentacaoInicial(mouse, funcionarioPadrao, 20);
		salvarMovimentacaoInicial(caboRede, funcionarioPadrao, 8);
	}

	private void criarAtivosPadrao(Funcionario funcionarioPadrao, List<Categoria> categorias) {
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

	private void salvarMovimentacaoInicial(Materiais material, Funcionario funcionario, int quantidade) {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setMaterial(material);
		movimentacao.setFuncionario(funcionario);
		movimentacao.setTipo(TipoMovimentacao.ENTRADA);
		movimentacao.setQuantidade(quantidade);
		movimentacao.setObservacao("estoque inicial");
		movimentacaoRepository.save(movimentacao);
	}

	private void criarFuncionarioAutorizado(String nome, String nif) {
		FuncionarioAutenticado funcionario = funcionarioAutenticadoRepository.findByNifAndAtivoTrue(nif)
				.orElse(new FuncionarioAutenticado());
		funcionario.setNome(nome);
		funcionario.setNif(nif);
		funcionario.setAtivo(true);
		funcionarioAutenticadoRepository.save(funcionario);
	}
}
