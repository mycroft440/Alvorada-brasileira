# Alvorada Brasileira 🇧🇷

Aplicativo Android em Kotlin/Jetpack Compose para acompanhar propostas legislativas, participação cidadã e fiscalização de obras públicas.

## Base atual

A interface principal segue o protótipo definido para o projeto:

- **Feed** — mostra somente propostas compartilhadas por usuários;
- **Propostas** — abas Aprovadas, Rejeitadas e Em progresso;
- **Obras do Gov** — acompanhamento e sinais explicáveis de risco para fiscalização;
- **Perfil** — aberto pelo avatar, com privacidade para dados sensíveis;
- navegação horizontal por gesto entre as três áreas principais;
- detalhes de tramitação, votos por partido e votos individuais simulados;
- compartilhamento de proposta com comentário próprio;
- like/dislike e comentários em modo de demonstração.

> Os dados da base visual são fictícios e servem apenas para desenvolvimento. A integração oficial será feita por uma API própria do Alvorada, alimentada por fontes governamentais.

## Arquitetura

A UI usa modelos próprios e acessa os dados por `AlvoradaRepository`. A implementação atual é `MockAlvoradaRepository`; isso permite substituir os mocks por backend/cache sem acoplar as telas ao formato das APIs externas.

Consulte [`docs/APP_ARCHITECTURE.md`](docs/APP_ARCHITECTURE.md) para o planejamento completo.

## Tecnologias

- Kotlin
- Jetpack Compose
- Material 3
- Compose Foundation Pager
- arquitetura orientada a Repository

## Próximas etapas

1. validar a base visual no Android;
2. adicionar persistência local;
3. criar backend Alvorada;
4. integrar Câmara e Senado/Congresso;
5. implementar backend social;
6. integrar bases de obras, contratos, licitações e pagamentos;
7. construir o motor auditável de sinais de risco.
