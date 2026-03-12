package br.senai.estoque.gerenciamento_estoque.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class ApiCategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping
	public ResponseEntity<?> listarCategorias(HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		return ResponseEntity.ok(categoriaService.listarTodas());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarCategoria(@PathVariable Long id, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			return ResponseEntity.ok(categoriaService.buscarPorId(id));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@PostMapping
	public ResponseEntity<?> salvarCategoria(@Valid @RequestBody Categoria categoria, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.salvar(categoria));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria,
			HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			return ResponseEntity.ok(categoriaService.atualizar(id, categoria));
		} catch (IllegalArgumentException ex) {
			HttpStatus status = ex.getMessage().contains("nao encontrada") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirCategoria(@PathVariable Long id, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			categoriaService.excluir(id);
			return ResponseEntity.ok(Map.of("mensagem", "categoria excluida com sucesso"));
		} catch (IllegalArgumentException ex) {
			HttpStatus status = ex.getMessage().contains("nao encontrada") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(Map.of("mensagem", ex.getMessage()));
		}
	}
}
