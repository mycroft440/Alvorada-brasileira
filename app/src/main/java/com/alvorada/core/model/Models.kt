package com.alvorada.core.model

enum class ProposalStatus {
    APPROVED,
    REJECTED,
    IN_PROGRESS
}

enum class LegislativeHouse {
    CAMERA,
    SENADO,
    CONGRESSO
}

enum class VoteChoice {
    YES,
    NO,
    ABSTENTION
}

enum class WorkStatus {
    ONGOING,
    COMPLETED,
    PLANNED
}

enum class RiskLevel {
    LOW,
    MODERATE,
    HIGH
}

enum class PrivacyLevel(val label: String) {
    PRIVATE("Só eu"),
    FOLLOWERS("Seguidores"),
    PUBLIC("Público")
}

data class VoteSummary(
    val yes: Int,
    val no: Int,
    val abstentions: Int
)

data class TimelineEvent(
    val title: String,
    val date: String,
    val completed: Boolean = true
)

data class PartyVoteSummary(
    val party: String,
    val yes: Int,
    val no: Int,
    val abstentions: Int
)

data class ParliamentarianVote(
    val name: String,
    val partyState: String,
    val vote: VoteChoice
)

data class Proposal(
    val id: Long,
    val code: String,
    val title: String,
    val summary: String,
    val status: ProposalStatus,
    val house: LegislativeHouse,
    val updatedLabel: String,
    val author: String,
    val theme: String,
    val voteSummary: VoteSummary? = null,
    val timeline: List<TimelineEvent> = emptyList(),
    val partyVotes: List<PartyVoteSummary> = emptyList(),
    val parliamentarianVotes: List<ParliamentarianVote> = emptyList(),
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val likesCount: Int = 0,
    val dislikesCount: Int = 0,
    val sourceLabel: String = "Dados de demonstração"
)

data class FeedPost(
    val id: Long,
    val userName: String,
    val userInitials: String,
    val publishedLabel: String,
    val proposalId: Long,
    val comment: String,
    val likesCount: Int = 0,
    val dislikesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0
)

data class RiskSignal(
    val title: String,
    val description: String,
    val level: RiskLevel
)

data class GovernmentWork(
    val id: Long,
    val title: String,
    val agency: String,
    val location: String,
    val status: WorkStatus,
    val investment: String,
    val originalBudget: String,
    val paidAmount: String,
    val progressPercent: Int,
    val expectedDelivery: String,
    val contract: String,
    val contractor: String,
    val bidders: Int,
    val amendments: Int,
    val delayDays: Int,
    val benchmarkDelta: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val signals: List<RiskSignal>,
    val milestones: List<TimelineEvent>,
    val lastUpdate: String,
    val sourceLabel: String = "Dados de demonstração"
)

data class UserProfile(
    val name: String,
    val handle: String,
    val cityState: String,
    val profession: String,
    val incomeRange: String,
    val pastVotes: Map<String, String>,
    val nextElectionIntentions: Map<String, String>,
    val federalSatisfaction: Int,
    val stateSatisfaction: Int,
    val citySatisfaction: Int
)
