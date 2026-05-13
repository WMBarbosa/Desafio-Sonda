# TaskManager

Monorepositório de gestão de tarefas com **back-end em Spring Boot** (OAuth2 Authorization Server com grant `password` e **JWT** no resource server) e **front-end em React (Vite + TypeScript)**.

```text
taskmanager/
├── backend/                 # API Spring Boot
└── frontend/
    └── taskmanagerFe/       # SPA React
```

---

## Índice

1. [Tecnologias utilizadas](#tecnologias-utilizadas)
2. [Estrutura do projeto (back-end)](#estrutura-do-projeto-back-end)
3. [Estrutura do projeto (front-end)](#estrutura-do-projeto-front-end)
4. [Pré-requisitos](#pré-requisitos)
5. [Como iniciar o back-end](#como-iniciar-o-back-end)
6. [Como iniciar o front-end](#como-iniciar-o-front-end)
7. [Variáveis de ambiente](#variáveis-de-ambiente)
8. [Autenticação e fluxo resumido](#autenticação-e-fluxo-resumido)
9. [Scripts úteis (front-end)](#scripts-úteis-front-end)

---

## Tecnologias utilizadas

### Back-end (`backend/`)

| Tecnologia | Uso |
|------------|-----|
| [Spring Boot 4](https://spring.io/projects/spring-boot) | Aplicação e convenções |
| [Java 21](https://openjdk.org/) | Linguagem e runtime |
| [Spring Security](https://spring.io/projects/spring-security) | Segurança |
| [Spring Authorization Server](https://spring.io/projects/spring-authorization-server) | OAuth2 (`/oauth2/token`, grant `password`) |
| [Spring OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html) | Validação JWT nas APIs |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | Persistência |
| [PostgreSQL](https://www.postgresql.org/) | Banco (perfil `prod`) |
| [H2](https://www.h2database.com/) | Disponível para cenários de teste/console |
| [Springdoc OpenAPI](https://springdoc.org/) | Documentação Swagger/OpenAPI |

### Front-end (`frontend/taskmanagerFe/`)

| Tecnologia | Uso |
|------------|-----|
| [React 18](https://react.dev/) | Interface e componentes |
| [TypeScript](https://www.typescriptlang.org/) | Tipagem estática |
| [Vite 8](https://vitejs.dev/) | Dev server, HMR e build |
| [React Router 6](https://reactrouter.com/) | Rotas e layouts protegidos |
| [Tailwind CSS 3](https://tailwindcss.com/) | Estilização utilitária |
| [Axios](https://axios-http.com/) | Cliente HTTP e interceptors (JWT) |
| [React Hook Form](https://react-hook-form.com/) + [Zod](https://zod.dev/) + [@hookform/resolvers](https://github.com/react-hook-form/resolvers) | Formulários e validação |
| [jwt-decode](https://github.com/auth0/jwt-decode) | Leitura de claims do access token |
| [Lucide React](https://lucide.dev/) | Ícones |

---

## Estrutura do projeto (back-end)

Árvore do pacote **`backend/`** (código-fonte e configuração Maven; pastas de build como `target/` e metadados de IDE como `.idea/` não entram no versionamento típico e podem ser ignoradas ao navegar o projeto):

```text
backend/
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
└── src/
    ├── main/
    │   ├── java/com/barbosa/taskmanager/
    │   │   ├── TaskmanagerApplication.java
    │   │   ├── api/
    │   │   │   └── controller/
    │   │   │       ├── TaskController.java
    │   │   │       ├── UserController.java
    │   │   │       └── controllerException/
    │   │   │           ├── CustomError.java
    │   │   │           └── GlobalExceptionHandler.java
    │   │   ├── application/
    │   │   │   └── dto/
    │   │   │       ├── UserDetailsDTO.java
    │   │   │       ├── exceptionsDto/
    │   │   │       │   ├── FieldMessage.java
    │   │   │       │   └── ValidationError.java
    │   │   │       ├── request/
    │   │   │       │   ├── TaskRequestDTO.java
    │   │   │       │   └── UserResquestDTO.java
    │   │   │       ├── response/
    │   │   │       │   ├── TaskResponseDTO.java
    │   │   │       │   └── UserResponseDTO.java
    │   │   │       └── service/
    │   │   │           ├── AuthService.java
    │   │   │           ├── TaskService.java
    │   │   │           ├── UserService.java
    │   │   │           └── exception/
    │   │   │               ├── DatabaseException.java
    │   │   │               ├── ForbiddenException.java
    │   │   │               └── ResourceNotFoundException.java
    │   │   ├── domain/
    │   │   │   └── model/
    │   │   │       ├── entities/
    │   │   │       │   ├── Role.java
    │   │   │       │   ├── Task.java
    │   │   │       │   └── User.java
    │   │   │       └── enums/
    │   │   │           ├── Prioridade.java
    │   │   │           └── Status.java
    │   │   └── infrastructure/
    │   │       └── config/
    │   │           ├── OpenApiConfig.java
    │   │           ├── customgrant/
    │   │           │   ├── CustomPasswordAuthenticationConverter.java
    │   │           │   ├── CustomPasswordAuthenticationProvider.java
    │   │           │   ├── CustomPasswordAuthenticationToken.java
    │   │           │   └── CustomUserAuthorities.java
    │   │           ├── repository/
    │   │           │   ├── TaskRepository.java
    │   │           │   └── UserRepository.java
    │   │           └── security/
    │   │               ├── AuthorizationServerConfig.java
    │   │               └── ResourceServerConfig.java
    │   └── resources/
    │       ├── application.properties
    │       ├── application-prod.properties
    │       ├── application-test.properties
    │       └── data.sql
    └── test/
        └── java/com/barbosa/taskmanager/
            └── TaskmanagerApplicationTests.java
```

Resumo por camada:

| Camada | Conteúdo |
|--------|----------|
| `api/controller` | REST (`TaskController`, `UserController`) e tratamento global de erros |
| `application/dto` | DTOs de request/response, validação de erro e serviços de aplicação |
| `domain/model` | Entidades JPA (`User`, `Task`, `Role`) e enums (`Status`, `Prioridade`) |
| `infrastructure/config` | OAuth2 custom grant, segurança, repositórios Spring Data e OpenAPI |

---

## Estrutura do projeto (front-end)

```text
frontend/taskmanagerFe/
├── index.html              # HTML de entrada do Vite
├── package.json
├── vite.config.ts          # Vite + PostCSS (Tailwind + Autoprefixer)
├── tailwind.config.js      # Configuração do Tailwind (content paths)
├── tsconfig.*.json         # TypeScript (app + node)
├── eslint.config.js
├── public/
└── src/
    ├── main.tsx            # Montagem do React
    ├── index.css           # Diretivas @tailwind
    ├── App.tsx             # Rotas principais
    ├── lib/
    │   └── api.ts          # Instância Axios (baseURL, Bearer, 401)
    ├── contexts/           # Auth (login OAuth2, roles, JWT)
    ├── components/         # Layout, formulários, badges, tabela ordenável
    ├── pages/              # Login, Dashboard, Tarefas, Usuários, Unauthorized
    ├── services/           # taskService, userService, authService (legado)
    ├── hooks/              # useSortableTable
    ├── schemas/            # Schemas Zod (login, tarefa)
    └── types/              # DTOs e tipos compartilhados
```

Principais rotas da SPA:

| Rota | Descrição |
|------|-----------|
| `/login` | Login (e-mail + senha → `POST /oauth2/token`) |
| `/dashboard` | Resumo e tarefas recentes (usuário autenticado) |
| `/tarefas` | Lista, busca e (admin) CRUD de tarefas |
| `/usuarios` | Lista paginada de usuários (**somente admin**) |
| `/unauthorized` | Acesso negado por perfil |

---

## Pré-requisitos

### Back-end

- [JDK 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/) **ou** use o wrapper `./mvnw` / `mvnw.cmd` na pasta do back-end
- [PostgreSQL](https://www.postgresql.org/) em execução, com banco e usuário compatíveis com `application-prod.properties` (perfil ativo padrão: `prod`)

### Front-end

- [Node.js](https://nodejs.org/) **18+** (recomendado LTS atual)
- npm (vem com o Node)

---

## Como iniciar o back-end

1. **Suba o PostgreSQL** e crie o banco, se ainda não existir (valores padrão do projeto):

   - URL padrão: `jdbc:postgresql://localhost:5432/taskmanager`
   - Usuário: `postgres`
   - Senha: `postgres`

   Ajuste via variáveis de ambiente ou editando `application-prod.properties` conforme seu ambiente.

2. **Entre na pasta do back-end** (a partir da raiz `taskmanager`):

   ```bash
   cd backend
   ```

3. **Execute a aplicação**:

   ```bash
   ./mvnw spring-boot:run
   ```

   No Windows (PowerShell):

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   Com Maven instalado globalmente:

   ```bash
   mvn spring-boot:run
   ```

4. **Porta HTTP**: por padrão o Spring Boot usa **`8080`**, salvo configuração explícita de `server.port` (por exemplo em `application-test.properties` ou variável de ambiente).

5. **Documentação da API** (Springdoc): após subir o servidor, consulte a UI OpenAPI/Swagger no host/porta do back-end (caminho típico: `/swagger-ui.html` ou equivalente da versão do springdoc em uso).

---

## Como iniciar o front-end

1. **Instale as dependências** (na pasta do front-end):

   ```bash
   cd frontend/taskmanagerFe
   npm install
   ```

2. **Configure variáveis de ambiente** (recomendado): crie um arquivo **`.env`** na raiz de `taskmanagerFe` (veja a seção seguinte). Sem o `.env`, o front usa os valores padrão do código (`api.ts` e `Authprovider.tsx`).

3. **Inicie o servidor de desenvolvimento**:

   ```bash
   npm run dev
   ```

4. **URL do front-end**: o Vite está configurado para **`http://localhost:3000`** (com `host: true` também acessível na rede local). Se a porta 3000 estiver ocupada, o Vite pode escolher outra (ex.: 3001); observe o endereço exibido no terminal.

5. **Alinhe CORS no back-end**: o perfil `prod` define `cors.origins` com `http://localhost:3000` e `http://localhost:5173`. Se o front rodar em outra origem, inclua-a em `CORS_ORIGINS` no back-end.

---

## Variáveis de ambiente

No **back-end**, variáveis comuns (via `application.properties` / ambiente):

| Variável / propriedade | Descrição |
|------------------------|-----------|
| `CLIENT_ID` / `CLIENT_SECRET` | Credenciais do client OAuth2 |
| `JWT_DURATION` | TTL do access token (segundos) |
| `CORS_ORIGINS` | Origens permitidas (lista separada por vírgula) |
| `SPRING_DATASOURCE_URL` | JDBC PostgreSQL |

Crie um arquivo **`.env`** na raiz de `frontend/taskmanagerFe` (variáveis expostas ao cliente devem começar com `VITE_`):

| Variável | Descrição | Padrão no código (se omitida) |
|----------|-----------|----------------------------------|
| `VITE_API_BASE_URL` | URL base da API (sem barra final desnecessária) | `http://localhost:8080` |
| `VITE_OAUTH_CLIENT_ID` | Client ID do OAuth2 (Authorization Server) | `myclientid` |
| `VITE_OAUTH_CLIENT_SECRET` | Client secret (usado em Basic Auth no token) | `myclientsecret` |

Exemplo de `.env` quando a API roda na porta **8888**:

```env
VITE_API_BASE_URL=http://localhost:8888
VITE_OAUTH_CLIENT_ID=myclientid
VITE_OAUTH_CLIENT_SECRET=myclientsecret
```

---

## Autenticação e fluxo resumido

1. O login chama **`POST {VITE_API_BASE_URL}/oauth2/token`** com `Content-Type: application/x-www-form-urlencoded`, corpo incluindo `grant_type=password`, `username` (e-mail), `password`, `scope=read write`, e cabeçalho **`Authorization: Basic`** com `client_id:client_secret` em Base64.
2. A resposta traz `access_token` (JWT), armazenado em `localStorage` como `access_token`.
3. Demais requisições Axios enviam **`Authorization: Bearer`** seguido do valor de `access_token`, exceto no próprio endpoint de token.
4. Respostas **401** removem o token e redirecionam para `/login`.
5. O JWT inclui claims de autoridades (`ROLE_ADMIN`, `ROLE_EMPLOYEE`); o front usa isso para `isAdmin` e rotas protegidas.

Endpoints REST usados pelo front (prefixo relativo à `VITE_API_BASE_URL`):

- `GET/POST /api/tarefas`, `PUT/DELETE /api/tarefas/{id}`, etc.
- `GET /users`, `GET /users/me`, `GET /users/{id}`

---

## Scripts úteis (front-end)

| Comando | Descrição |
|---------|-----------|
| `npm run dev` | Servidor de desenvolvimento Vite |
| `npm run build` | Typecheck (`tsc -b`) + build de produção |
| `npm run preview` | Servir a pasta `dist` localmente |
| `npm run lint` | ESLint |
