## Autenticação

A API usa JWT. Antes de acessar os endpoints protegidos, faça login:

POST /auth/login
Content-Type: application/json

{
  "login": "gerente",
  "senha": "senha123"
}

A resposta traz um token. Envie-o nas próximas requisições no header:

Authorization: Bearer <token>

### Usuários de teste (pré-cadastrados via data.sql)
| Login    | Senha     | Perfil    |
|----------|-----------|-----------|
| gerente  | senha123  | GERENTE   |
| mecanico | senha123  | MECANICO  |

### Endpoint público (não exige token)
GET /ordens-servicos/{id}/status