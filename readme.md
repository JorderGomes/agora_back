# 🏛️ Ágora - Sistema de Gestão de Eventos

A **Ágora** é uma API REST robusta desenvolvida em Spring Boot para a organização e gestão de eventos, participantes e presenças. O nome remete às praças centrais da Grécia Antiga, locais de assembleia e eventos públicos.

> ⚠️ **Status do Projeto:** 🚧 Em construção. Implementando a camada de tratamento global de exceções e segurança.
> 

---

## 🛠️ Tecnologias Utilizadas

- **Java 17** & **Spring Boot 4.0.2**
- **Spring Data JPA** (Persistência de dados)
- **H2 Database** (Banco de dados em memória para desenvolvimento)
- **Lombok** (Produtividade e redução de boilerplate)
- **MapStruct** (Mapeamento performático de objetos)
- **JUnit 5** & **Mockito** (Suíte de testes unitários)
- **Bean Validation** (Validação de dados de entrada)

---

## 🏗️ Padrões de Projeto (Design Patterns)

O projeto foi estruturado seguindo padrões de mercado para garantir escalabilidade e facilidade de manutenção:

- **Data Transfer Object (DTO):** Utilizamos `Records` do Java para o tráfego de dados entre as camadas, garantindo a imutabilidade e evitando a exposição indesejada das entidades JPA.
- **Service Layer:** Toda a regra de negócio está centralizada na camada de serviço, mantendo os controllers "magros" (Thin Controllers).
- **Repository Pattern:** Abstração total da camada de dados utilizando o Spring Data JPA.
- **Mapper Pattern:** Uso do **MapStruct** para separar a lógica de conversão entre Entidade e DTO, evitando códigos repetitivos e manuais.
- **Specification Pattern:** Implementado no `EventService` para permitir buscas dinâmicas e filtragens complexas de eventos de forma elegante e desacoplada.

---

## 🛡️ Boas Práticas e Princípios de Engenharia

Para manter o código limpo e profissional, aplicamos os seguintes conceitos:

- **Testes Unitários (TDD Mindset):** Cobertura completa dos serviços (`UserService`, `EventService`, `RegisterService`) utilizando Mocks para isolar as dependências e garantir que as regras de negócio funcionem de forma independente.
- **Injeção de Dependências por Construtor:** Uso do `@RequiredArgsConstructor` do Lombok em campos `final`, seguindo a recomendação atual do Spring para facilitar a testabilidade e garantir a imutabilidade das dependências.
- **Global Exception Handling:** Centralização do tratamento de erros através de `@ControllerAdvice`, garantindo que a API responda com códigos HTTP semânticos (404, 400, 403, 409) e mensagens padronizadas.
- **Separação de Preocupações (SoC):** Cada classe possui uma responsabilidade única e bem definida.
- **Fail-Fast:** Validação rigorosa de dados logo na entrada da requisição através de anotações do Jakarta Validation.

---

## 📈 Próximos Passos

- [ ]  Implementação de Spring Security para controle de acesso.
- [ ]  Documentação da API com Swagger/OpenAPI.

---
