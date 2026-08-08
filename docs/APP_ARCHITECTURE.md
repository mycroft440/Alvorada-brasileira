# Arquitetura planejada — Alvorada Brasileira

Este documento define a base técnica do app conforme o protótipo aprovado na conversa. A primeira etapa usa dados simulados para validar navegação e experiência. Dados políticos, legislativos, obras e perfis mostrados no mock não devem ser tratados como informação real.

## 1. Navegação principal

O app possui três destinos principais, navegáveis por toque na barra inferior e por gesto horizontal:

1. **Feed** — mostra apenas propostas que foram compartilhadas por usuários a partir da aba Propostas.
2. **Propostas** — separa propostas em Aprovadas, Rejeitadas e Em progresso.
3. **Obras do Gov** — acompanha obras e exibe sinais de risco para fiscalização.

O **Perfil** é aberto pelo avatar no topo e não ocupa um quarto botão na navegação inferior.

## 2. Estrutura de código da base

```text
app/src/main/java/com/alvorada/
├── MainActivity.kt
├── core/
│   └── model/
│       └── Models.kt
├── data/
│   └── AlvoradaRepository.kt
└── ui/
    ├── AlvoradaApp.kt
    ├── screens/
    │   ├── MainScreens.kt
    │   └── DetailSheets.kt
    └── theme/
        └── Theme.kt
```

A estrutura é pequena de propósito. Quando as integrações remotas começarem, ela poderá ser dividida em módulos sem reescrever a interface.

## 3. Modelos de domínio

A UI trabalha com modelos próprios do Alvorada, e nunca diretamente com JSON da Câmara, Senado ou outros portais.

Principais modelos:

- `Proposal`
- `VoteSummary`
- `PartyVoteSummary`
- `ParliamentarianVote`
- `TimelineEvent`
- `FeedPost`
- `GovernmentWork`
- `RiskSignal`
- `UserProfile`
- `PrivacyLevel`

Essa decisão permite trocar a origem dos dados sem alterar os componentes visuais.

## 4. Camada de dados

A interface `AlvoradaRepository` é o limite entre UI e dados.

Na fase inicial:

```text
UI -> AlvoradaRepository -> MockAlvoradaRepository
```

Na fase de produção:

```text
UI
 -> Repository
    -> banco local/cache
    -> API Alvorada
       -> coletor Câmara
       -> coletor Senado/Congresso
       -> backend social
       -> coletor de obras/contratos/despesas
```

O Android não deverá depender diretamente das APIs governamentais para montar cada tela. O backend Alvorada fará coleta, normalização, deduplicação, cache e histórico.

## 5. Feed social

Regra principal: **o Feed não lista automaticamente toda proposta oficial**.

Uma proposta aparece no Feed quando um usuário a compartilha. O compartilhamento contém:

- referência imutável à proposta oficial;
- comentário opcional do usuário;
- likes/dislikes da publicação;
- comentários;
- compartilhamentos.

O fato legislativo e a opinião do usuário devem ficar visualmente separados.

### Backend futuro

Tabelas/recursos esperados:

```text
users
feed_posts
comments
reactions
shares
followed_proposals
moderation_reports
```

## 6. Propostas legislativas

A tela possui três estados de produto:

- `APPROVED`
- `REJECTED`
- `IN_PROGRESS`

Esses estados não devem ser definidos procurando palavras como “aprovado” no texto da última movimentação. O backend deverá interpretar o processo completo e manter também o status oficial bruto.

Cada proposta pode conter:

- código, tipo, número e ano;
- ementa/resumo;
- autoria;
- casa atual e casa de origem;
- tema;
- tramitação completa;
- votações;
- placar;
- votos por partido;
- votos nominais por parlamentar;
- fonte oficial e data de sincronização.

### Coleta futura

Estratégia planejada:

1. carga histórica em lote;
2. normalização Câmara/Senado/Congresso;
3. deduplicação da mesma matéria entre casas;
4. sincronização incremental;
5. armazenamento do payload bruto para auditoria;
6. geração de eventos legislativos quando o status mudar.

## 7. Obras do Gov e fiscalização

A área de obras não deve declarar automaticamente que existe fraude ou corrupção. O sistema calcula **sinais de risco** que priorizam análise humana.

Exemplos de sinais:

- custo atual muito acima do orçamento inicial;
- aditivos sucessivos;
- baixa concorrência em licitação;
- pagamento incompatível com avanço físico;
- atraso anormal;
- preço acima de benchmark de obras semelhantes;
- empresa sancionada ou impedida;
- vínculos societários relevantes;
- concentração de contratos;
- alterações incomuns de escopo;
- divergências entre bases públicas.

O `riskScore` deve ser explicável: toda pontuação precisa apontar quais sinais contribuíram para ela.

### Regra de linguagem

- correto: “risco alto”, “anomalia”, “indício”, “merece auditoria”;
- incorreto sem fonte oficial: “empresa corrupta”, “obra fraudulenta”, “crime comprovado”.

## 8. Perfil e privacidade

O perfil poderá armazenar:

- cidade/estado;
- profissão;
- faixa de renda;
- voto passado declarado pelo próprio usuário;
- intenção de voto para a próxima eleição;
- satisfação com governo federal, estadual e municipal.

Renda, voto, intenção de voto e avaliação política são tratados como dados sensíveis no produto. O padrão de visibilidade é **Só eu**, com opção explícita de mudar para Seguidores ou Público.

O app nunca deverá apresentar o voto declarado pelo usuário como dado confirmado pela Justiça Eleitoral.

## 9. Próximas camadas

### Fase A — base visual (atual)

- Compose;
- navegação Feed/Propostas/Obras por swipe;
- dados simulados;
- detalhes de propostas e obras;
- compartilhamento local para validar o fluxo;
- perfil e controles visuais de privacidade.

### Fase B — persistência local

- Room;
- cache de propostas e obras;
- DataStore para preferências;
- estado offline.

### Fase C — backend Alvorada

- API própria;
- autenticação;
- usuários;
- feed social;
- comentários/reactions;
- moderação;
- sincronização oficial.

### Fase D — coletores legislativos

- Câmara;
- Senado/Congresso;
- deduplicação;
- histórico;
- votações nominais e orientações partidárias.

### Fase E — fiscalização de obras

- contratos/licitações;
- pagamentos/empenhos;
- obras físicas;
- empresas;
- sanções;
- benchmarks;
- motor de sinais de risco;
- evidências e denúncias moderadas.

## 10. Princípios de implementação

1. Fonte oficial sempre identificável.
2. Dado oficial separado de opinião do usuário.
3. Dado simulado claramente marcado.
4. Sem inferir voto nominal quando a votação não fornece registro individual completo.
5. Sem acusar fraude/corrupção a partir de score automático.
6. Privacidade restritiva por padrão para dados sensíveis.
7. Repositórios isolam a UI das fontes externas.
8. O backend normaliza dados antes de entregá-los ao Android.
9. Toda transformação importante deve ser testável.
10. Payload bruto da fonte deve ser preservado para auditoria e reprocessamento.
