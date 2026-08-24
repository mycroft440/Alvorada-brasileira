package com.alvorada.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.alvorada.core.model.FeedPost
import com.alvorada.core.model.GovernmentWork
import com.alvorada.core.model.Proposal
import com.alvorada.data.AlvoradaRepository
import com.alvorada.ui.screens.CommentsSheet
import com.alvorada.ui.screens.FeedScreen
import com.alvorada.ui.screens.ProfileSheet
import com.alvorada.ui.screens.ProposalDetailSheet
import com.alvorada.ui.screens.ProposalsScreen
import com.alvorada.ui.screens.ShareProposalSheet
import com.alvorada.ui.screens.WorkDetailSheet
import com.alvorada.ui.screens.WorksScreen
import kotlinx.coroutines.launch

private data class MainDestination(
    val label: String,
    val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlvoradaApp(repository: AlvoradaRepository) {
    val destinations = remember {
        listOf(
            MainDestination("Feed", "⌂"),
            MainDestination("Propostas", "▤"),
            MainDestination("Obras do Gov", "🏗")
        )
    }
    val pagerState = rememberPagerState(pageCount = { destinations.size })
    val scope = rememberCoroutineScope()
    val proposals = remember(repository) { repository.proposals() }
    val rawWorks = remember(repository) { repository.works() }
    val works = remember(rawWorks) { rawWorks.prioritizedForIntegrityReview() }
    val profile = remember(repository) { repository.profile() }
    val feedPosts = remember(repository) {
        mutableStateListOf<FeedPost>().apply { addAll(repository.feed()) }
    }

    var selectedProposal by remember { mutableStateOf<Proposal?>(null) }
    var selectedWork by remember { mutableStateOf<GovernmentWork?>(null) }
    var shareProposal by remember { mutableStateOf<Proposal?>(null) }
    var commentPost by remember { mutableStateOf<FeedPost?>(null) }
    var showProfile by remember { mutableStateOf(false) }

    fun goToPage(index: Int) {
        scope.launch { pagerState.animateScrollToPage(index) }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = { goToPage(index) },
                        icon = { Text(destination.emoji) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .then(Modifier)
        ) { page ->
            when (page) {
                0 -> FeedScreen(
                    contentPadding = padding,
                    posts = feedPosts,
                    proposals = proposals,
                    onProfileClick = { showProfile = true },
                    onProposalClick = { selectedProposal = it },
                    onCommentClick = { commentPost = it }
                )

                1 -> ProposalsScreen(
                    contentPadding = padding,
                    proposals = proposals,
                    onProfileClick = { showProfile = true },
                    onProposalClick = { selectedProposal = it },
                    onShareClick = { shareProposal = it }
                )

                else -> WorksScreen(
                    contentPadding = padding,
                    works = works,
                    onProfileClick = { showProfile = true },
                    onWorkClick = { presentedWork ->
                        selectedWork = rawWorks.firstOrNull { it.id == presentedWork.id } ?: presentedWork
                    }
                )
            }
        }
    }

    selectedProposal?.let { proposal ->
        ModalBottomSheet(onDismissRequest = { selectedProposal = null }) {
            ProposalDetailSheet(
                proposal = proposal,
                onShare = {
                    selectedProposal = null
                    shareProposal = proposal
                }
            )
        }
    }

    selectedWork?.let { work ->
        ModalBottomSheet(onDismissRequest = { selectedWork = null }) {
            WorkDetailSheet(work = work)
        }
    }

    shareProposal?.let { proposal ->
        ModalBottomSheet(onDismissRequest = { shareProposal = null }) {
            ShareProposalSheet(
                proposal = proposal,
                onPublish = { text ->
                    val nextId = (feedPosts.maxOfOrNull { it.id } ?: 100L) + 1L
                    feedPosts.add(
                        index = 0,
                        element = FeedPost(
                            id = nextId,
                            userName = profile.name,
                            userInitials = profile.name
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .take(2)
                                .joinToString("") { it.first().uppercase() },
                            publishedLabel = "agora",
                            proposalId = proposal.id,
                            comment = text,
                            sharesCount = 1
                        )
                    )
                    shareProposal = null
                    goToPage(0)
                }
            )
        }
    }

    commentPost?.let { post ->
        ModalBottomSheet(onDismissRequest = { commentPost = null }) {
            CommentsSheet(post = post)
        }
    }

    if (showProfile) {
        ModalBottomSheet(onDismissRequest = { showProfile = false }) {
            ProfileSheet(profile = profile)
        }
    }
}
