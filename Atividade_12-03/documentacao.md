````markdown
# Situação de Aprendizagem BackEnd  
## Projeto: Sistema de Gerenciamento de Estoque e Ativos Patrimoniais

## Descrição

Os alunos serão encarregados de desenvolver um sistema de gerenciamento de estoque e ativos patrimoniais para uma unidade escolar do SENAI-SP.

Este sistema ajudará a escola a acompanhar e controlar os materiais utilizados em laboratórios, salas de aula e outras áreas, além de gerenciar os ativos patrimoniais da instituição.

---

## Plano de Atividades

### Levantamento de Requisitos

Baseado nos conhecimentos do SENAI, os alunos devem documentar os requisitos funcionais e não funcionais do sistema, incluindo:

- a lista de materiais a serem controlados;
- os dados a serem registrados para cada ativo patrimonial.

### Design do Banco de Dados

Com base nos requisitos levantados, os alunos devem projetar o banco de dados para armazenar informações sobre estoque e ativos patrimoniais.

O banco de dados deve incluir tabelas para:

- materiais;
- categorias de materiais;
- entradas e saídas de estoque;
- informações de ativos patrimoniais.

### Implementação da API

Usando Spring Boot, os alunos devem implementar uma API RESTful para gerenciar estoque e ativos patrimoniais.

Devem criar endpoints para:

- adicionar;
- visualizar;
- atualizar;
- excluir:

  - materiais;
  - categorias de materiais;
  - movimentações de estoque;
  - ativos patrimoniais.

### Desenvolvimento da Interface de Usuário

Os alunos devem criar uma interface de usuário intuitiva e responsiva para interagir com a API.

A interface deve permitir aos usuários realizar operações como:

- adicionar novos materiais;
- registrar movimentações de estoque;
- visualizar inventários;
- gerenciar ativos patrimoniais.

> **Obs.:** Utilizar o Manual de Identidade do SENAI para criar as telas, utilizando cores, fontes e logos corretamente.  
> Link: https://cronos-media.sesisenaisp.org.br/api/media/1-0/files?file=arq_81_221108_4e633672-b0e8-4502-9096-172179043b17.pdf&disposition=false

---

## Entregáveis

- Link da aplicação com o código da atividade;
- `README.md` junto com o código, devendo conter os RF e RNF;
- Schema do banco de dados gerado, podendo ser:
  - print da tela;
  - schema das tabelas.

---

## Critérios de Avaliação

### Levantamento de Requisitos

- Documentação clara e completa dos requisitos funcionais e não funcionais do sistema;
- Evidência de compreensão das necessidades da unidade escolar do SENAI-SP.

### Design do Banco de Dados

- Modelo de banco de dados bem estruturado e normalizado;
- Uso adequado de relacionamentos entre tabelas para representar as entidades do sistema.

### Implementação da API

- Funcionalidade completa e correta dos endpoints da API;
- Adoção de boas práticas de codificação, como separação de responsabilidades e uso de padrões de projeto.

### Desenvolvimento da Interface de Usuário

- Interface de usuário intuitiva e amigável;
- Funcionalidades completas para interagir com todas as operações disponíveis na API.

### Usabilidade e Experiência do Usuário

- Facilidade de uso da interface de usuário.

### Inovação e Criatividade

- Implementação de funcionalidades adicionais ou soluções criativas que vão além dos requisitos mínimos do projeto;
- Demonstração de pensamento crítico e capacidade de encontrar soluções originais para desafios técnicos.

---

# Tutorial Passo-a-passo da Atividade

## 1. Pré-requisitos

- Java JDK 17 (ou a versão exigida pelo curso);
- VS Code instalado.

### Extensões do VS Code

- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack (VMware)
- *(Opcional)* Maven for Java ou Gradle for Java

---

## 2. Criar o projeto pelo VS Code (Spring Initializr)

1. Abra o VS Code.
2. Pressione `Ctrl + Shift + P` para abrir a Command Palette.
3. Digite e selecione:  
   `Spring Initializr: Create a Maven Project`  
   ou  
   `Spring Initializr: Create a Gradle Project`.
4. Selecione a linguagem **Java**.
5. Escolha a versão do Spring Boot.  
   Recomendo `4.x` se estiver usando Java 21+.
6. Informe o **Group Id**.  
   Exemplo: `br.senai.estoque`
7. Informe o **Artifact Id**.  
   Exemplo: `gerenciamento-estoque`
8. Escolha o **Packaging**:
   - `Jar` (mais comum)
9. Escolha a versão do Java:
   - `17` ou `21`
10. Selecione as dependências. Exemplo para API REST:
    - Spring Web
    - Spring Data JPA
    - Spring Boot Dev Tools
    - H2 Database *(para testes)* ou PostgreSQL Driver / MySQL Driver *(se for usar banco real)*
    - Thymeleaf
    - *(Opcional)* Lombok
11. Selecione a pasta onde o projeto será criado e confirme.
12. Quando o VS Code perguntar, clique em **Open** para abrir o projeto gerado.

---

## 3. Conferir a estrutura do projeto

Verifique se foram criados:

- `pom.xml` *(Maven)* ou `build.gradle` *(Gradle)*
- `src/main/java/...` *(código)*
- `src/main/resources/application.properties` *(configurações)*
- Classe principal `...Application.java` com `@SpringBootApplication`

---

## 4. Teste rápido (primeiro endpoint)

Crie um controller simples:

`src/main/java/.../controller/PingController.java`

### Exemplo de endpoint

- `GET /ping` retornando `"ok"`

Teste no navegador ou no Postman/Insomnia:

```text
http://localhost:8080/ping
````

---

## 5. Dica para configurar o banco (`application.properties`)

### Para H2 (teste)

```properties
spring.datasource.url=jdbc:h2:mem:estoque
spring.h2.console.enabled=true
```

### Para PostgreSQL (exemplo)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque
spring.datasource.username=...
spring.datasource.password=...
spring.jpa.hibernate.ddl-auto=update
```

---

# Iniciando o Desenvolvimento

## Criação dos Models para Desenvolvimento da Atividade

Model para login dos funcionários: somente funcionário com o NIF (Número de Identidade Funcional) cadastrado no banco pode acessar o sistema.

---

## 6. Passo-a-passo: criando os Models (entidades JPA) em uma package `model`

### 6.1. Criar a package `model`

No VS Code:

1. Abra o Explorer e vá em `src/main/java`.
2. Encontre o pacote base do projeto
   Exemplo: `br.senai.estoque.gerenciamentoestoque`
3. Clique com o botão direito no pacote base e selecione:

   * **New Package**
4. Nomeie como:

   * `...model`

### Padrão sugerido de camadas

* `br.senai.estoque...model`
* `br.senai.estoque...repository`
* `br.senai.estoque...service`
* `br.senai.estoque...controller`

### 6.2. Adicionar dependências (se ainda não estiverem no projeto)

* `spring-boot-starter-data-jpa`
* Driver do banco *(H2, PostgreSQL ou MySQL)*
* *(Opcional)* Lombok

### 6.3. Regras gerais para os Models (boas práticas)

* Cada Model vira uma tabela (`@Entity`);
* Use `@Id` e `@GeneratedValue` para a chave primária;
* Use `@Column(unique = true)` quando for um campo único *(ex.: nif)*;
* Para relacionamentos, use:

  * `@ManyToOne` *(muitos para um)*
  * `@OneToMany` *(um para muitos)*
  * `@OneToOne` *(um para um)*
* Evite `@Data` do Lombok em entidades com relacionamentos. Prefira `@Getter` e `@Setter`.

---

## 7. Demonstração: Model de login do funcionário (NIF)

### 7.1. Criar `Funcionario.java` em `...model`

Crie o arquivo:

`src/main/java/.../model/Funcionario.java`

Exemplo *(sem Spring Security, validando por NIF e senha)*:

```java
package br.senai.estoque.gerenciamentoestoque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true, length = 20)
	private String nif;

	@Column(nullable = false)
	private String senha;

	@Column(nullable = false)
	private boolean ativo = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
```

### 7.2. Como este Model atende o requisito do login

* O `nif` é único no banco;
* No endpoint de login, a regra fica:

> Se existir funcionário com `nif` e a senha corresponder, então pode acessar.

### 7.3. Verificação do Funcionário para Criação do Cadastro

A ideia é ter uma tabela de pré-cadastro *(lista branca)* com `nif` e `nome`.

No cadastro, o sistema só permite criar a conta se existir um registro correspondente nessa tabela.

### 7.3.1. Criar o Model de pré-cadastro: `FuncionarioAutenticado`

Crie o arquivo:

`src/main/java/.../model/FuncionarioAutenticado.java`

```java
package br.senai.estoque.gerenciamentoestoque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios_autenticados")
public class FuncionarioAutenticado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(nullable = false, length = 20)
	private String nif;

	@Column(nullable = false)
	private boolean ativo = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
```

### 7.3.2. Como usar essa tabela na regra do cadastro

#### Regra do cadastro

Só cadastra se existir em `funcionarios_autenticados` um registro com o mesmo `nif` e `nome` e com `ativo = true`.

Se não existir, retornar erro:

> “NIF e nome não estão autorizados para cadastro.”

**Observação:** a entidade `FuncionarioAutenticado` não guarda senha.
Ela serve apenas para validar se a pessoa está autorizada a criar a conta.

---

## 8. Passo-a-passo: criando os Repositories (CRUD) para os Models

### 8.1. Criar a package `repository`

No VS Code, em `src/main/java/...`:

1. Clique com o botão direito no pacote base do projeto;
2. Selecione **New Package**;
3. Nomeie como:

   * `...repository`

### 8.2. Regras gerais dos Repositories

Um Repository é uma interface que estende `JpaRepository`.

O Spring já entrega o CRUD básico:

* `save(...)`
* `findById(...)`
* `findAll()`
* `deleteById(...)`

Você pode criar métodos por nome *(query methods)*, por exemplo:

* `findByNif(String nif)`
* `existsByNifAndAtivoTrue(String nif)`

### 8.3. Criar `FuncionarioRepository.java`

Crie o arquivo:

`src/main/java/.../repository/FuncionarioRepository.java`

```java
package br.senai.estoque.gerenciamentoestoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamentoestoque.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	Optional<Funcionario> findByNif(String nif);
	boolean existsByNif(String nif);
}
```

### 8.4. Criar `FuncionarioAutenticadoRepository.java`

Crie o arquivo:

`src/main/java/.../repository/FuncionarioAutenticadoRepository.java`

```java
package br.senai.estoque.gerenciamentoestoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamentoestoque.model.FuncionarioAutenticado;

public interface FuncionarioAutenticadoRepository extends JpaRepository<FuncionarioAutenticado, Long> {
	Optional<FuncionarioAutenticado> findByNifAndAtivoTrue(String nif);
	boolean existsByNifAndNomeAndAtivoTrue(String nif, String nome);
}
```

### 8.5. Exemplos de uso (no Service ou Controller)

#### Login

Buscar o funcionário por `nif` e validar a senha.

#### Cadastro

Verificar se está autorizado na lista branca:

```java
existsByNifAndNomeAndAtivoTrue(nif, nome)
```

---

## 9. Templates Thymeleaf: fragments (`header/footer`) + `index`

### 9.1. Estrutura sugerida

Crie a estrutura em `src/main/resources/templates`:

* `templates/fragments/header.html`
* `templates/fragments/footer.html`
* `templates/index.html`
* `templates/auth/login.html`
* `templates/auth/cadastro.html`

E em `src/main/resources/static`:

* `static/css/style.css`
* `static/img/logo-senai.svg` *(ou `.png`)*

---

### 9.2. Fragmento: `fragments/header.html`

Header com navegação para Cadastro e Login, seguindo a identidade visual SENAI-SP.

```html
<header th:fragment="header" class="senai-header">
	<div class="senai-shell">
		<a class="senai-brand" th:href="@{/}">
			<img class="senai-logo" th:src="@{/img/logo-senai.svg}" alt="SENAI-SP"/>
			<span class="senai-brand-text">Sistema de Estoque e Patrimônio</span>
		</a>

		<nav class="senai-nav" aria-label="Navegação principal">
			<a class="senai-link" th:href="@{/cadastro}">Cadastro</a>
			<a class="senai-button" th:href="@{/login}">Login</a>
		</nav>
	</div>
</header>
```

---

### 9.3. Fragmento: `fragments/footer.html`

```html
<footer th:fragment="footer" class="senai-footer">
	<div class="senai-shell">
		<p class="senai-footer-text">© <span th:text="${#dates.format(#dates.createNow(), 'yyyy')}"></span> SENAI-SP. Todos os direitos reservados.</p>
	</div>
</footer>
```

---

### 9.4. Tela: `index.html` (incluindo header e footer)

```html
<!DOCTYPE html>
<html lang="pt-br" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta charset="UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title>Início | Sistema de Estoque e Patrimônio</title>

		<link rel="stylesheet" th:href="@{/css/style.css}"/>
	</head>
	<body>
		<div th:include="~{fragments/header :: header}"></div>

		<main class="senai-main">
			<section class="senai-hero">
				<div class="senai-shell">
					<h1 class="senai-title">Gerenciamento de Estoque e Ativos Patrimoniais</h1>
					<p class="senai-subtitle">Controle de materiais, movimentações e inventário da unidade escolar.</p>

					<div class="senai-actions">
						<a class="senai-button" th:href="@{/login}">Acessar</a>
						<a class="senai-link" th:href="@{/cadastro}">Criar conta</a>
					</div>
				</div>
			</section>
		</main>

		<div th:include="~{fragments/footer :: footer}"></div>
	</body>
</html>
```

---

### 9.5. CSS base: `static/css/style.css`

Ajustando as cores, tipografia e espaçamentos conforme o Manual de Identidade do SENAI-SP.

```css
:root {
	--senai-red: #E30613;
	--senai-red-dark: #B0000A;
	--senai-white: #FFFFFF;
	--senai-gray-900: #1F2937;
	--senai-gray-50: #F9FAFB;
	--senai-gray-200: #E5E7EB;
	--senai-radius: 10px;
	--senai-container: 1100px;
	--senai-font: system-ui, -apple-system, "Segoe UI", Roboto, Arial, sans-serif;
}

* { box-sizing: border-box; }

body {
	margin: 0;
	font-family: var(--senai-font);
	color: var(--senai-gray-900);
	background: var(--senai-gray-50);
}

.senai-shell {
	max-width: var(--senai-container);
	margin: 0 auto;
	padding: 0 16px;
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.senai-header {
	background: var(--senai-white);
	border-bottom: 4px solid var(--senai-red);
}

.senai-brand {
	display: flex;
	align-items: center;
	gap: 12px;
	text-decoration: none;
	color: inherit;
	padding: 14px 0;
}

.senai-logo {
	height: 32px;
	width: auto;
}

.senai-brand-text {
	font-weight: 700;
}

.senai-nav {
	display: flex;
	align-items: center;
	gap: 14px;
}

.senai-link {
	text-decoration: none;
	color: var(--senai-gray-900);
	font-weight: 600;
}

.senai-link:hover {
	text-decoration: underline;
}

.senai-button {
	display: inline-block;
	text-decoration: none;
	background: var(--senai-red);
	color: var(--senai-white);
	padding: 10px 14px;
	border-radius: var(--senai-radius);
	font-weight: 700;
}

.senai-button:hover {
	background: var(--senai-red-dark);
}

.senai-main { padding: 28px 0; }

.senai-hero {
	background: var(--senai-white);
	border: 1px solid var(--senai-gray-200);
	border-radius: var(--senai-radius);
	padding: 28px 0;
}

.senai-title { margin: 0; font-size: 28px; }
.senai-subtitle { margin: 10px 0 0; color: #4B5563; }

.senai-actions {
	margin-top: 18px;
	display: flex;
	gap: 14px;
	align-items: center;
}

.senai-footer {
	margin-top: 28px;
	background: var(--senai-white);
	border-top: 1px solid var(--senai-gray-200);
}

.senai-footer-text {
	margin: 0;
	padding: 18px 0;
	font-size: 14px;
	color: #6B7280;
}
```

---

## 10. Telas: Login e Cadastro (Thymeleaf)

Crie os arquivos em `src/main/resources/templates/auth/`:

* `login.html`
* `cadastro.html`

---

### 10.1. `templates/auth/login.html`

```html
<!DOCTYPE html>
<html lang="pt-br" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta charset="UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title>Login | Sistema de Estoque e Patrimônio</title>
		<link rel="stylesheet" th:href="@{/css/style.css}"/>
	</head>
	<body>
		<div th:include="~{fragments/header :: header}"></div>

		<main class="senai-main">
			<section class="senai-hero">
				<div class="senai-shell" style="justify-content:center;">
					<div class="senai-card" style="width: 100%; max-width: 520px;">
						<h1 class="senai-title" style="font-size: 22px;">Login</h1>
						<p class="senai-subtitle" style="margin-top: 6px;">Acesse com NIF e senha.</p>

						<div th:if="${erro}" class="senai-alert senai-alert-error" role="alert">
							<p th:text="${erro}"></p>
						</div>

						<div th:if="${sucesso}" class="senai-alert senai-alert-success" role="status">
							<p th:text="${sucesso}"></p>
						</div>

						<form th:action="@{/auth/login}" method="post" class="senai-form" autocomplete="on">
							<label class="senai-label" for="nif">NIF</label>
							<input class="senai-input" type="text" id="nif" name="nif" placeholder="Digite seu NIF" required maxlength="20"/>

							<label class="senai-label" for="senha">Senha</label>
							<input class="senai-input" type="password" id="senha" name="senha" placeholder="Digite sua senha" required/>

							<button class="senai-button" type="submit" style="width:100%; margin-top: 12px;">Entrar</button>
						</form>

						<p style="margin: 12px 0 0;">
							Não tem conta?
							<a class="senai-link" th:href="@{/cadastro}">Criar conta</a>
						</p>
					</div>
				</div>
			</section>
		</main>

		<div th:include="~{fragments/footer :: footer}"></div>
	</body>
</html>
```

---

### 10.2. `templates/auth/cadastro.html`

```html
<!DOCTYPE html>
<html lang="pt-br" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta charset="UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title>Cadastro | Sistema de Estoque e Patrimônio</title>
		<link rel="stylesheet" th:href="@{/css/style.css}"/>
	</head>
	<body>
		<div th:include="~{fragments/header :: header}"></div>

		<main class="senai-main">
			<section class="senai-hero">
				<div class="senai-shell" style="justify-content:center;">
					<div class="senai-card" style="width: 100%; max-width: 520px;">
						<h1 class="senai-title" style="font-size: 22px;">Cadastro</h1>
						<p class="senai-subtitle" style="margin-top: 6px;">Crie sua conta (somente NIF autorizado).</p>

						<div th:if="${erro}" class="senai-alert senai-alert-error" role="alert">
							<p th:text="${erro}"></p>
						</div>

						<div th:if="${sucesso}" class="senai-alert senai-alert-success" role="status">
							<p th:text="${sucesso}"></p>
						</div>

						<form th:action="@{/auth/cadastro}" method="post" class="senai-form" autocomplete="on">
							<label class="senai-label" for="nome">Nome</label>
							<input class="senai-input" type="text" id="nome" name="nome" placeholder="Digite seu nome" required maxlength="120"/>

							<label class="senai-label" for="nif">NIF</label>
							<input class="senai-input" type="text" id="nif" name="nif" placeholder="Digite seu NIF" required maxlength="20"/>

							<label class="senai-label" for="senha">Senha</label>
							<input class="senai-input" type="password" id="senha" name="senha" placeholder="Crie uma senha" required minlength="4"/>

							<button class="senai-button" type="submit" style="width:100%; margin-top: 12px;">Criar conta</button>
						</form>

						<p style="margin: 12px 0 0;">
							Já tem conta?
							<a class="senai-link" th:href="@{/login}">Fazer login</a>
						</p>
					</div>
				</div>
			</section>
		</main>

		<div th:include="~{fragments/footer :: footer}"></div>
	</body>
</html>
```

---

### 10.3. CSS opcional para melhorar o layout

Adicione ao final do seu `static/css/style.css`:

```css
.senai-card {
	background: var(--senai-white);
	border: 1px solid var(--senai-gray-200);
	border-radius: var(--senai-radius);
	padding: 18px;
}

.senai-form {
	margin-top: 14px;
	display: grid;
	gap: 8px;
}

.senai-label {
	font-weight: 700;
	font-size: 14px;
}

.senai-input {
	width: 100%;
	padding: 10px 12px;
	border: 1px solid var(--senai-gray-200);
	border-radius: var(--senai-radius);
	font: inherit;
	background: var(--senai-white);
}

.senai-input:focus {
	outline: none;
	border-color: var(--senai-red);
	box-shadow: 0 0 0 3px rgba(227, 6, 19, 0.15);
}

.senai-alert {
	border-radius: var(--senai-radius);
	padding: 10px 12px;
	margin-top: 12px;
}

.senai-alert-error {
	border: 1px solid #FCA5A5;
	background: #FEF2F2;
	color: #991B1B;
}

.senai-alert-success {
	border: 1px solid #86EFAC;
	background: #F0FDF4;
	color: #166534;
}

.senai-alert p { margin: 0; }
```

---

## 11. Controllers: rotas para index, login, cadastro, página interna e logout

### 11.1. `HomeController` (rota da página inicial)

Crie o arquivo:

`src/main/java/.../controller/HomeController.java`

```java
package br.senai.estoque.gerenciamentoestoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "index"; // templates/index.html
	}
}
```

---

### 11.2. `AuthController` (rotas de login, cadastro, página interna e logout)

Crie o arquivo:

`src/main/java/.../controller/AuthController.java`

```java
package br.senai.estoque.gerenciamentoestoque.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	// TODO: inserir os acessos ao Service/Repository

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login"; // templates/auth/login.html
	}

	@PostMapping("/login")
	public String login(@RequestParam String nif,
						@RequestParam String senha,
						HttpSession session,
						Model model) {
		// TODO: validar nif/senha no Service
		boolean credenciaisOk = false;

		if (!credenciaisOk) {
			model.addAttribute("erro", "NIF ou senha inválidos.");
			return "auth/login";
		}

		// Sessão simples: marca que o usuário está logado
		session.setAttribute("usuarioLogado", true);
		session.setAttribute("nif", nif);

		// Após login, manda para a página interna
		return "redirect:/app";
	}

	@GetMapping("/cadastro")
	public String cadastroPage() {
		return "auth/cadastro"; // templates/auth/cadastro.html
	}

	@PostMapping("/cadastro")
	public String cadastro(@RequestParam String nome,
						  @RequestParam String nif,
						  @RequestParam String senha,
						  Model model) {
		// TODO: validar se (nif,nome) está autorizado na tabela funcionarios_autenticados
		// TODO: salvar funcionario com senha (ideal: hash)
		model.addAttribute("erro", "Implementar cadastro no Service.");
		return "auth/cadastro";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
```

---

## 12. Página interna da aplicação (dashboard) + controller

A página interna será acessada após o login e terá um botão para sair.

### 12.1. Criar o controller: `AppController`

Crie o arquivo:

`src/main/java/.../controller/AppController.java`

```java
package br.senai.estoque.gerenciamentoestoque.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	@GetMapping("/app")
	public String appHome(HttpSession session, Model model) {
		Object usuarioLogado = session.getAttribute("usuarioLogado");
		if (usuarioLogado == null || !(Boolean) usuarioLogado) {
			return "redirect:/login";
		}

		model.addAttribute("nif", session.getAttribute("nif"));
		return "app/index"; // templates/app/index.html
	}
}
```

---

### 12.2. Criar a tela interna: `templates/app/index.html`

Crie o arquivo:

`src/main/resources/templates/app/index.html`

```html
<!DOCTYPE html>
<html lang="pt-br" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta charset="UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title>Área Interna | Sistema de Estoque e Patrimônio</title>
		<link rel="stylesheet" th:href="@{/css/style.css}"/>
	</head>
	<body>
		<div th:include="~{fragments/header :: header}"></div>

		<main class="senai-main">
			<section class="senai-hero">
				<div class="senai-shell" style="justify-content:center;">
					<div class="senai-card" style="width: 100%;">
						<h1 class="senai-title" style="font-size: 22px;">Área interna</h1>
						<p class="senai-subtitle" style="margin-top: 6px;">Você está logado.</p>

						<p style="margin: 12px 0 0;" th:if="${nif}">
							<strong>NIF:</strong> <span th:text="${nif}"></span>
						</p>

						<div class="senai-actions" style="margin-top: 16px;">
							<a class="senai-button" th:href="@{/logout}">Logout</a>
							<a class="senai-link" th:href="@{/}">Voltar para a tela inicial</a>
						</div>

						<hr style="margin: 18px 0; border: 0; border-top: 1px solid #E5E7EB;"/>

						<h2 style="margin: 0; font-size: 18px;">Próximos passos</h2>
						<ul style="margin: 10px 0 0;">
							<li>Criar as telas de Materiais, Categorias, Movimentações e Ativos.</li>
							<li>Conectar os botões às rotas e aos endpoints da API.</li>
						</ul>
					</div>
				</div>
			</section>
		</main>

		<div th:include="~{fragments/footer :: footer}"></div>
	</body>
</html>
```

---
