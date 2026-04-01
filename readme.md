# 🏛️ Ágora - Sistema de Gestão de Eventos

A **Ágora** é uma API REST robusta desenvolvida em Spring Boot para a organização e gestão de eventos, participantes e presenças. O nome remete às praças centrais da Grécia Antiga, locais de assembleia e eventos públicos.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17** & **Spring Boot 4.0.2**
- **Spring Security** (Autenticação e Autorização)
- **Auth0 JWT** (Token-based authentication)
- **SpringDoc OpenAPI 3.0.0** (Documentação Swagger)
- **Spring Data JPA** (Persistência de dados)
- **H2 Database** (Banco de dados em memória)
- **MapStruct 1.6.3** (Mapeamento performático de objetos)
- **Lombok** & **Bean Validation**

---

## 🛡️ Segurança e Autenticação

A arquitetura de segurança foi desenhada para ser **Stateless**, garantindo escalabilidade e proteção rigorosa dos dados:

- **JWT (JSON Web Token):** Autenticação baseada em tokens com identificação de usuário (`Subject`) e ID único nas `Claims` para otimização de consultas.
- **Segurança de Senhas:** Implementação de hashing com **BCrypt** 
- **Controle de Acesso por Contexto:** Validação manual na camada de serviço para garantir que:
    * Somente o próprio usuário logado edite seus dados.
    * Somente o **organizador** de um evento possa atualizar ou excluí-lo.
    * Participantes possam gerenciar apenas suas próprias inscrições.

---

## 🏗️ Padrões de Projeto (Design Patterns)

O projeto foi estruturado seguindo padrões de mercado para garantir escalabilidade e facilidade de manutenção:

- **Data Transfer Object (DTO):** Utilizamos `Records` do Java para o tráfego de dados entre as camadas, garantindo a imutabilidade e evitando a exposição indesejada das entidades JPA.
- **Service Layer:** Toda a regra de negócio está centralizada na camada de serviço, mantendo os controllers "magros" (Thin Controllers).
- **Repository Pattern:** Abstração total da camada de dados utilizando o Spring Data JPA.
- **Mapper Pattern:** Uso do **MapStruct** para separar a lógica de conversão entre Entidade e DTO, evitando códigos repetitivos e manuais.
- **Specification Pattern:** Implementado no `EventService` para permitir buscas dinâmicas e filtragens complexas de eventos de forma elegante e desacoplada.
- **RBAC (Role-Based Access Control):** Diferenciação de permissões entre usuários comuns (`USER`) e administradores (`ADMIN`).
- **Global Exception Handling:** Tratamento centralizado de erros com respostas JSON padronizadas e códigos HTTP semânticos (401, 403, 404, 409).

---

## 🛡️ Boas Práticas e Princípios de Engenharia

Para manter o código limpo e profissional, aplicamos os seguintes conceitos:

- **Testes Unitários (TDD Mindset):** Cobertura completa dos serviços (`UserService`, `EventService`, `RegisterService`) utilizando Mocks para isolar as dependências e garantir que as regras de negócio funcionem de forma independente.
- **Injeção de Dependências por Construtor:** Uso do `@RequiredArgsConstructor` do Lombok em campos `final`, seguindo a recomendação atual do Spring para facilitar a testabilidade e garantir a imutabilidade das dependências.
- **Global Exception Handling:** Centralização do tratamento de erros através de `@ControllerAdvice`, garantindo que a API responda com códigos HTTP semânticos (404, 400, 403, 409) e mensagens padronizadas.
- **Separação de Preocupações (SoC):** Cada classe possui uma responsabilidade única e bem definida.
- **Fail-Fast:** Validação rigorosa de dados logo na entrada da requisição através de anotações do Jakarta Validation.

---

## 📖 Documentação da API

A documentação interativa da API (Swagger UI) pode ser acessada localmente após a execução do projeto através do link:

`http://localhost:8080/swagger-ui/index.html`


