package com.alvorada.data

import com.alvorada.core.model.*

interface AlvoradaRepository {
    fun proposals(): List<Proposal>
    fun feed(): List<FeedPost>
    fun works(): List<GovernmentWork>
    fun profile(): UserProfile
}

/**
 * Dados exclusivamente de demonstração para a base visual do app.
 * Esta implementação será substituída pelos repositórios remotos e locais
 * quando o backend Alvorada e as integrações oficiais forem conectados.
 */
class MockAlvoradaRepository : AlvoradaRepository {
    override fun proposals(): List<Proposal> = proposalData

    override fun feed(): List<FeedPost> = listOf(
        FeedPost(
            id = 101,
            userName = "Juliana Ferreira",
            userInitials = "JF",
            publishedLabel = "há 3 horas",
            proposalId = 1,
            comment = "Transparência é fundamental. Quero acompanhar como essa proposta será aplicada na prática.",
            likesCount = 1_200,
            dislikesCount = 385,
            commentsCount = 58,
            sharesCount = 212
        ),
        FeedPost(
            id = 102,
            userName = "Carlos Henrique",
            userInitials = "CH",
            publishedLabel = "há 6 horas",
            proposalId = 5,
            comment = "A proposta parece importante, mas ainda quero entender o texto e a tramitação antes de formar opinião.",
            likesCount = 642,
            dislikesCount = 128,
            commentsCount = 34,
            sharesCount = 96
        )
    )

    override fun works(): List<GovernmentWork> = listOf(
        GovernmentWork(
            id = 1,
            title = "Duplicação da BR-101 — trecho de demonstração",
            agency = "Órgão rodoviário — simulação",
            location = "Santa Catarina",
            status = WorkStatus.ONGOING,
            investment = "R$ 320 milhões",
            originalBudget = "R$ 245 milhões",
            paidAmount = "R$ 276 milhões",
            progressPercent = 68,
            expectedDelivery = "Dez/2026",
            contract = "Contrato DEMO-08/2025",
            contractor = "Consórcio de demonstração",
            bidders = 1,
            amendments = 5,
            delayDays = 164,
            benchmarkDelta = "+22%",
            riskScore = 78,
            riskLevel = RiskLevel.HIGH,
            signals = listOf(
                RiskSignal("Crescimento do orçamento", "Valor atual muito acima do orçamento inicial da simulação.", RiskLevel.HIGH),
                RiskSignal("Muitos aditivos", "Cinco alterações contratuais justificam revisão detalhada.", RiskLevel.MODERATE),
                RiskSignal("Baixa concorrência", "A simulação possui apenas um licitante.", RiskLevel.MODERATE),
                RiskSignal("Pagamento x avanço físico", "Percentual pago está acima do avanço físico informado.", RiskLevel.HIGH)
            ),
            milestones = listOf(
                TimelineEvent("Licitação concluída", "2025", true),
                TimelineEvent("Terraplenagem", "2026", true),
                TimelineEvent("Pavimentação", "Em andamento", false),
                TimelineEvent("Sinalização final", "Pendente", false)
            ),
            lastUpdate = "07/08/2026"
        ),
        GovernmentWork(
            id = 2,
            title = "Construção de escola — projeto de demonstração",
            agency = "Órgão educacional — simulação",
            location = "São Paulo",
            status = WorkStatus.ONGOING,
            investment = "R$ 12 milhões",
            originalBudget = "R$ 10,8 milhões",
            paidAmount = "R$ 3,9 milhões",
            progressPercent = 25,
            expectedDelivery = "Ago/2027",
            contract = "Contrato DEMO-41/2026",
            contractor = "Construtora de demonstração",
            bidders = 4,
            amendments = 2,
            delayDays = 27,
            benchmarkDelta = "+6%",
            riskScore = 43,
            riskLevel = RiskLevel.MODERATE,
            signals = listOf(
                RiskSignal("Aditivos recentes", "Duas alterações contratuais merecem acompanhamento.", RiskLevel.MODERATE),
                RiskSignal("Atraso de cronograma", "Execução simulada está 27 dias atrás do planejado.", RiskLevel.MODERATE)
            ),
            milestones = listOf(
                TimelineEvent("Fundação", "Concluída", true),
                TimelineEvent("Estrutura", "Em andamento", false),
                TimelineEvent("Acabamento", "Pendente", false)
            ),
            lastUpdate = "06/08/2026"
        ),
        GovernmentWork(
            id = 3,
            title = "Estação de tratamento de água — demonstração",
            agency = "Serviço municipal — simulação",
            location = "Minas Gerais",
            status = WorkStatus.COMPLETED,
            investment = "R$ 45 milhões",
            originalBudget = "R$ 44 milhões",
            paidAmount = "R$ 44,6 milhões",
            progressPercent = 100,
            expectedDelivery = "Concluída",
            contract = "Contrato DEMO-03/2023",
            contractor = "Engenharia de demonstração",
            bidders = 6,
            amendments = 1,
            delayDays = 0,
            benchmarkDelta = "-2%",
            riskScore = 12,
            riskLevel = RiskLevel.LOW,
            signals = listOf(
                RiskSignal("Sem anomalias relevantes", "Custos e cronograma estão próximos aos parâmetros da simulação.", RiskLevel.LOW)
            ),
            milestones = listOf(
                TimelineEvent("Obra civil", "Concluída", true),
                TimelineEvent("Equipamentos", "Concluídos", true),
                TimelineEvent("Testes", "Concluídos", true),
                TimelineEvent("Entrega", "Concluída", true)
            ),
            lastUpdate = "18/06/2024"
        )
    )

    override fun profile(): UserProfile = UserProfile(
        name = "José Gustavo",
        handle = "@josegustavo",
        cityState = "Varginha, Minas Gerais",
        profession = "Comerciante",
        incomeRange = "R$ 3.000 a R$ 5.000",
        pastVotes = linkedMapOf(
            "Vereador" to "Adicionar em quem votei",
            "Deputado estadual" to "Adicionar em quem votei",
            "Deputado federal" to "Adicionar em quem votei",
            "Senador" to "Adicionar em quem votei",
            "Governador" to "Adicionar em quem votei",
            "Presidente" to "Adicionar em quem votei"
        ),
        nextElectionIntentions = linkedMapOf(
            "Vereador" to "Adicionar intenção de voto",
            "Deputado estadual" to "Adicionar intenção de voto",
            "Deputado federal" to "Adicionar intenção de voto",
            "Senador" to "Adicionar intenção de voto",
            "Governador" to "Adicionar intenção de voto",
            "Presidente" to "Adicionar intenção de voto"
        ),
        federalSatisfaction = 3,
        stateSatisfaction = 4,
        citySatisfaction = 2
    )

    private val proposalData = listOf(
        Proposal(
            id = 1,
            code = "PL 1234/2026",
            title = "Transparência em contratos públicos",
            summary = "Amplia a publicação de dados sobre contratos e despesas de órgãos federais.",
            status = ProposalStatus.APPROVED,
            house = LegislativeHouse.CAMERA,
            updatedLabel = "há 2h",
            author = "Autoria fictícia — demonstração",
            theme = "Transparência",
            voteSummary = VoteSummary(312, 121, 8),
            timeline = listOf(
                TimelineEvent("Apresentado", "03/02/2026"),
                TimelineEvent("Aprovado em comissão", "18/04/2026"),
                TimelineEvent("Aprovado no plenário", "08/08/2026")
            ),
            partyVotes = listOf(
                PartyVoteSummary("ABC", 38, 3, 1),
                PartyVoteSummary("XYZ", 12, 41, 2),
                PartyVoteSummary("DEF", 31, 17, 4)
            ),
            parliamentarianVotes = listOf(
                ParliamentarianVote("Ana Ribeiro", "ABC/MG", VoteChoice.YES),
                ParliamentarianVote("Carlos Nunes", "XYZ/SP", VoteChoice.NO),
                ParliamentarianVote("Marina Lopes", "DEF/BA", VoteChoice.YES),
                ParliamentarianVote("Paulo Freitas", "ABC/PR", VoteChoice.ABSTENTION)
            ),
            commentsCount = 1_200,
            sharesCount = 4_700,
            likesCount = 8_400,
            dislikesCount = 3_100
        ),
        Proposal(
            id = 2,
            code = "PL 1488/2026",
            title = "Ampliação de bolsas para formação técnica",
            summary = "Amplia vagas financiadas para cursos técnicos e profissionalizantes.",
            status = ProposalStatus.APPROVED,
            house = LegislativeHouse.CAMERA,
            updatedLabel = "ontem",
            author = "Autoria fictícia — demonstração",
            theme = "Educação",
            voteSummary = VoteSummary(287, 96, 14),
            timeline = listOf(
                TimelineEvent("Apresentado", "11/01/2026"),
                TimelineEvent("Aprovado em comissão", "20/05/2026"),
                TimelineEvent("Aprovado no plenário", "07/08/2026")
            ),
            commentsCount = 542,
            sharesCount = 1_300,
            likesCount = 4_100,
            dislikesCount = 1_200
        ),
        Proposal(
            id = 3,
            code = "PL 870/2026",
            title = "Mudanças nas regras de publicidade digital",
            summary = "Proposta de demonstração rejeitada em votação nominal.",
            status = ProposalStatus.REJECTED,
            house = LegislativeHouse.SENADO,
            updatedLabel = "há 5h",
            author = "Autoria fictícia — demonstração",
            theme = "Tecnologia",
            voteSummary = VoteSummary(29, 46, 3),
            timeline = listOf(
                TimelineEvent("Apresentado", "05/03/2026"),
                TimelineEvent("Aprovado em comissão", "12/06/2026"),
                TimelineEvent("Rejeitado no plenário", "08/08/2026")
            ),
            commentsCount = 846,
            sharesCount = 2_100,
            likesCount = 2_200,
            dislikesCount = 6_800
        ),
        Proposal(
            id = 4,
            code = "PL 932/2026",
            title = "Regras de cobrança de serviços digitais",
            summary = "A proposta fictícia não alcançou votos suficientes para aprovação.",
            status = ProposalStatus.REJECTED,
            house = LegislativeHouse.CAMERA,
            updatedLabel = "2 dias atrás",
            author = "Autoria fictícia — demonstração",
            theme = "Economia digital",
            voteSummary = VoteSummary(171, 238, 11),
            commentsCount = 903,
            sharesCount = 2_800,
            likesCount = 1_900,
            dislikesCount = 5_400
        ),
        Proposal(
            id = 5,
            code = "PL 2044/2026",
            title = "Programa nacional de recuperação de nascentes",
            summary = "Encaminhado para análise da comissão responsável por meio ambiente.",
            status = ProposalStatus.IN_PROGRESS,
            house = LegislativeHouse.CAMERA,
            updatedLabel = "há 18 min",
            author = "Autoria fictícia — demonstração",
            theme = "Meio ambiente",
            timeline = listOf(
                TimelineEvent("Apresentado", "01/08/2026"),
                TimelineEvent("Distribuído à comissão", "08/08/2026"),
                TimelineEvent("Próxima etapa: parecer", "Aguardando", false)
            ),
            commentsCount = 96,
            sharesCount = 214,
            likesCount = 721,
            dislikesCount = 84
        ),
        Proposal(
            id = 6,
            code = "PL 2234/2026",
            title = "Valorização dos professores da educação básica",
            summary = "Institui, na simulação, uma nova política nacional para a educação básica.",
            status = ProposalStatus.IN_PROGRESS,
            house = LegislativeHouse.SENADO,
            updatedLabel = "há 5h",
            author = "Autoria fictícia — demonstração",
            theme = "Educação",
            timeline = listOf(
                TimelineEvent("Apresentado", "15/07/2026"),
                TimelineEvent("Audiência pública", "04/08/2026"),
                TimelineEvent("Relatoria designada", "08/08/2026")
            ),
            commentsCount = 633,
            sharesCount = 1_600,
            likesCount = 3_500,
            dislikesCount = 580
        )
    )
}
