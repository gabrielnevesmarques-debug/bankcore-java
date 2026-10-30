# BankCore

API REST de operações bancárias, desenvolvida como projeto de portfólio para praticar Java, Spring Boot, Spring Data JPA e PostgreSQL.

> **Aviso:** projeto educacional. Não movimenta dinheiro real e não deve ser utilizado como sistema bancário de produção.

## Funcionalidades

- Criar e consultar contas.
- Consultar saldo.
- Realizar depósitos e saques.
- Transferir valores entre contas.
- Consultar o histórico de transações.
- Validar valores e impedir saques com saldo insuficiente.
- Executar transferências dentro de uma transação do banco de dados.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Bean Validation

## Como executar

Pré-requisitos: JDK 21, Docker e Docker Compose.

1. Clone o repositório.
2. Na pasta do projeto, inicie o PostgreSQL:

   ```bash
   docker compose up -d
   ```

3. Execute a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

   Se o projeto ainda não tiver o Maven Wrapper, use `mvn spring-boot:run` com Maven instalado.

A API ficará disponível em `http://localhost:8080`.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/accounts` | Criar conta |
| GET | `/api/accounts` | Listar contas |
| GET | `/api/accounts/{id}` | Consultar conta e saldo |
| POST | `/api/accounts/{id}/deposit` | Depositar |
| POST | `/api/accounts/{id}/withdraw` | Sacar |
| POST | `/api/accounts/{id}/transfer` | Transferir |
| GET | `/api/accounts/{id}/transactions` | Histórico da conta |

## Exemplos de requisição

Criar uma conta:

```http
POST /api/accounts
Content-Type: application/json

{
  "ownerName": "Ana Silva"
}
```

Depositar:

```http
POST /api/accounts/ID_DA_CONTA/deposit
Content-Type: application/json

{
  "amount": 150.00
}
```

Transferir:

```http
POST /api/accounts/ID_DA_ORIGEM/transfer
Content-Type: application/json

{
  "destinationAccountId": "UUID_DA_CONTA_DESTINO",
  "amount": 50.00
}
```

## Organização do código

- `controller`: recebe as requisições HTTP.
- `service`: concentra as regras de negócio.
- `repository`: acessa o banco de dados.
- `model`: representa contas e transações.
- `dto`: define os dados de entrada e saída da API.
- `exception`: padroniza respostas de erro.

## Próximas evoluções

- Testes automatizados para as regras de negócio e endpoints.
- Autenticação e autorização.
- Migrações de banco com Flyway.
- Documentação interativa com Swagger/OpenAPI.
- Pipeline de integração contínua.

---
Desenvolvido para fins de estudo e portfólio.
