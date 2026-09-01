# Mecânica API

Sistema de gestão para oficina mecânica, desenvolvido como projeto da Fase 1 da pós-graduação em Arquitetura de Software (FIAP). Permite o cadastro de clientes e veículos, controle de estoque de peças, catálogo de serviços, e o acompanhamento completo do ciclo de vida de uma Ordem de Serviço (OS) — da abertura até a entrega ao cliente.

## Índice

- [Sobre o projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Como executar localmente](#como-executar-localmente)
- [Autenticação](#autenticação)
- [Dados de exemplo (seed)](#dados-de-exemplo-seed)
- [Documentação da API](#documentação-da-api)
- [Testes e cobertura](#testes-e-cobertura)
- [Análise de vulnerabilidades](#análise-de-vulnerabilidades)
- [Estrutura do projeto](#estrutura-do-projeto)

## Sobre o projeto

A aplicação modela o fluxo de uma oficina mecânica: um cliente chega com seu veículo, é feito o cadastro, uma Ordem de Serviço é aberta, passa por diagnóstico, orçamento (peças + serviços), aprovação do cliente, execução, e finalmente entrega. O sistema também controla o estoque de peças, garantindo que uma OS só avance quando há peças disponíveis — ou fica pendente aguardando reposição.

Fluxo de status de uma Ordem de Serviço:

```
CRIADA
  → EM_DIAGNOSTICO
    → DIAGNOSTICO_CONCLUIDO
      → PENDENTE_ESTOQUE (se faltar peça) → volta para DIAGNOSTICO_CONCLUIDO após reposição
      → PENDENTE_NOTIFICACAO_CLIENTE
        → PENDENTE_APROVACAO_CLIENTE
          → APROVADA → EM_EXECUCAO → FINALIZADA → PENDENTE_ENTREGA → ENTREGUE
          → CANCELADA
```

Uma OS também pode ser cancelada a partir de `PENDENTE_ESTOQUE`, caso o cliente desista da espera pela peça.

## Arquitetura

Monolito organizado em arquitetura em camadas:

```
controller   → recebe as requisições HTTP e retorna as respostas
service      → contém as regras de negócio
repository   → acesso a dados (Spring Data JPA)
entity       → entidades JPA mapeadas para o banco
model        → DTOs de entrada e saída
mapper       → conversão entre entidades e DTOs
validation   → validações de dados de entrada
security     → configuração de autenticação/autorização (JWT)
exception    → tratamento centralizado de erros
```

## Tecnologias utilizadas

- **Java 25**
- **Spring Boot 4** (Web MVC, Data JPA, Security)
- **PostgreSQL** — banco de dados principal
- **H2** — banco em memória, usado exclusivamente nos testes automatizados
- **JWT** (jjwt) — autenticação stateless
- **Springdoc OpenAPI** — documentação da API (Swagger)
- **JaCoCo** — cobertura de testes
- **SonarQube** (Community Edition) — análise estática de código e cobertura
- **OWASP Dependency-Check** — análise de vulnerabilidades em dependências
- **Docker / Docker Compose** — containerização e orquestração do ambiente
- **Maven** — build e gerenciamento de dependências


## Como executar localmente

### Pré-requisitos

- [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose instalados

### Subindo a aplicação

Com o Docker em execução, na raiz do projeto:

```bash
docker compose up -d --build
```

Isso vai:
1. Subir um container PostgreSQL (porta `5432`)
2. Buildar a imagem da aplicação a partir do `Dockerfile`
3. Subir a aplicação (porta `8080`), aguardando o banco estar pronto
4. Popular o banco automaticamente com dados de exemplo (ver seção [Dados de exemplo](#dados-de-exemplo-seed))

Para acompanhar os logs:

```bash
docker compose logs -f app
```

A aplicação estará disponível em `http://localhost:8080`.


### Rodando os testes localmente (sem Docker)

Os testes usam H2 em memória e não dependem do Postgres:

```bash
./mvnw clean test
```

## Autenticação

A API utiliza autenticação via **JWT**. Os usuários são pré-cadastrados no banco (não há endpoint de criação de usuário) e possuem dois perfis:

| Perfil | Login | Senha | Permissões |
|---|---|---|---|
| Gerente | `gerente` | `senha123` | Acesso total (clientes, veículos, serviços, estoque, ordens de serviço) |
| Mecânico | `mecanico` | `senha123` | Pode criar e atualizar ordens de serviço; não pode gerenciar clientes, veículos, serviços ou estoque |

### Obtendo o token

```http
POST /auth/login
Content-Type: application/json

{
  "login": "gerente",
  "senha": "senha123"
}
```

A resposta contém o token JWT, que deve ser enviado no header `Authorization: Bearer <token>` nas demais requisições.

### Endpoints públicos (não exigem autenticação)

- `POST /auth/login`
- `GET /ordens-servicos/{id}` — consulta de uma ordem de serviço
- `GET /ordens-servicos/{id}/status` — consulta do status de uma ordem de serviço
- `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**` — documentação da API

Todos os demais endpoints exigem autenticação.

## Dados de exemplo (seed)

Ao subir a aplicação pela primeira vez, o banco é populado automaticamente com dados de exemplo, permitindo testar a API imediatamente sem necessidade de cadastro manual:

- **5 clientes**, cada um com seu veículo
- **10 peças** em estoque
- **10 serviços** cadastrados
- **6 ordens de serviço** em diferentes estágios do fluxo:
  - OS 1: ciclo completo até `ENTREGUE`, com 1 dia de duração em cada fase
  - OS 2: ciclo completo até `ENTREGUE`, incluindo 15 dias parada em `PENDENTE_ESTOQUE` (aguardando reposição de peça)
  - OS 3: em andamento, status `EM_EXECUCAO`
  - OS 4: concluída (`ENTREGUE`), com durações de fase mais longas e variadas
  - OS 5: `CANCELADA` após espera prolongada por uma peça indisponível
  - OS 6: recém-criada, em `EM_DIAGNOSTICO`, ainda sem peças/serviços vinculados

Essa massa de dados foi pensada para permitir testar imediatamente o endpoint de métricas de tempo médio por fase (`GET /ordens-servicos/metricas/tempo-medio-por-fase`), já que há variação real de tempo entre as ordens.

A recriação desses dados é idempotente: mesmo reiniciando a aplicação sem apagar o volume do banco, os dados não são duplicados.

## Documentação da API

Com a aplicação em execução, a documentação interativa (Swagger UI) está disponível em:

```
http://localhost:8080/swagger-ui.html
```

O JSON da especificação OpenAPI pode ser obtido em:

```
http://localhost:8080/v3/api-docs
```

### Collection Postman

Uma collection do Postman pronta para uso está disponível em [`mecanica.postman_collection.json`](./mecanica.postman_collection.json), já organizada por recurso e com exemplos preenchidos com os dados do seed. Basta importar no Postman e executar o request **"Login (gerente)"** primeiro — o token é salvo automaticamente e reutilizado nos demais requests.

## Testes e cobertura

O projeto possui testes unitários e de integração, com cobertura nos domínios críticos (controllers e services principais), medida via **JaCoCo**.

```bash
./mvnw clean verify
```

O relatório de cobertura é gerado em `target/site/jacoco/index.html`.

## Análise de vulnerabilidades

Foram utilizadas duas ferramentas complementares para análise de segurança e qualidade do código:

- **SonarQube (Community Edition)**: análise estática de código, cobertura de testes e identificação de vulnerabilidades/code smells no próprio código-fonte.
- **OWASP Dependency-Check**: análise de vulnerabilidades conhecidas (CVEs) nas dependências do projeto, executado automaticamente na fase `verify` do Maven, gerando um relatório em `target/dependency-check-report/dependency-check-report.html`.

Os relatórios completos (cobertura e vulnerabilidades) estão anexados na entrega, em formato PDF.

## Estrutura do projeto

```
mecanica/
├── src/
│   ├── main/
│   │   ├── java/com/postech/mecanica/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── validation/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       ├── java/com/postech/mecanica/
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```