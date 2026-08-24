package com.alvorada.ui

import com.alvorada.core.model.GovernmentWork
import com.alvorada.core.model.RiskLevel

/**
 * Ordena as obras para fiscalização, exibindo primeiro os maiores indícios de
 * irregularidade. O resultado é apenas uma priorização de auditoria e não uma
 * afirmação de fraude ou corrupção.
 */
internal fun List<GovernmentWork>.prioritizedForIntegrityReview(): List<GovernmentWork> =
    map { work ->
        work.copy(
            signals = work.signals
                .sortedByDescending { signal -> riskWeight(signal.level) }
                .map { signal ->
                    signal.copy(
                        title = "${signal.title}: ${signal.description}"
                    )
                }
        )
    }.sortedWith(
        compareByDescending<GovernmentWork> { it.riskScore }
            .thenByDescending { work -> work.signals.count { it.level == RiskLevel.HIGH } }
            .thenByDescending { it.signals.size }
    )

private fun riskWeight(level: RiskLevel): Int = when (level) {
    RiskLevel.HIGH -> 3
    RiskLevel.MODERATE -> 2
    RiskLevel.LOW -> 1
}
