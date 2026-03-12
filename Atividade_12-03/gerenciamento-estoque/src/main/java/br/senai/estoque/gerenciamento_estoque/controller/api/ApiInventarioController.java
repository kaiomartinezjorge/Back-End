package br.senai.estoque.gerenciamento_estoque.controller.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/inventario")
public class ApiInventarioController {

	@Autowired
	private MaterialService materialService;

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping
	public ResponseEntity<?> visualizarInventario(HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("mensagem", "acesso nao autorizado"));
		}
		Map<String, Object> resposta = new LinkedHashMap<>();
		resposta.put("materiais", materialService.listarTodos());
		resposta.put("ativosPatrimoniais", ativoPatrimonialService.listarTodos());
		return ResponseEntity.ok(resposta);
	}
}
