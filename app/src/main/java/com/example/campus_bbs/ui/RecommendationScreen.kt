package com.example.campus_bbs.ui

import android.location.Geocoder
import android.location.LocationManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.pullrefresh.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalContext
import androidx.core.location.LocationCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.MainViewModel
import com.example.campus_bbs.utils.LocationUtils
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import kotlinx.coroutines.coroutineScope

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
fun RecommendationScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
//    val blogList = remember{ mutableStateOf<List<Blog>>(FakeDataGenerator().generateFakeBlogs(10)) }

    val titles = listOf("Default", "Hot", "Subscribe")

    var pagerState = rememberPagerState(0)

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index, 0F)
                                }
                            },
                            modifier = Modifier
                        ) {
                            Text(text = title, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }
        }

        HorizontalPager(
            pageCount = titles.size,
            state = pagerState
        ) {

            page ->
            when(page) {
                0 -> DefaultRecommendation(mainAppNavController, Modifier, mainViewModel)
                1 -> HotRecommendation()
                2 -> SubscribedRecommendation()
            }
        }
    }
}

@Composable

fun HotRecommendation(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        item {
            RichText(
                modifier = Modifier.padding(16.dp)
            ) {
                Markdown(
                    """
                # Markdown Demo
            
                Emphasis, aka italics, with *asterisks* or _underscores_. Strong emphasis, aka bold, with **asterisks** or __underscores__. Combined emphasis with **asterisks and _underscores_**. [Links with two blocks, text in square-brackets, destination is in parentheses.](https://www.example.com). Inline `code` has `back-ticks around` it.
            
                1. First ordered list item
                2. Another item
                    * Unordered sub-list.
                3. And another item.
                    You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).
            
                * Unordered list can use asterisks
                - Or minuses
                + Or pluses
                ---
            
                ```javascript
                var s = "code blocks use monospace font";
                alert(s);
                ```
            
                Markdown | Table | Extension
                --- | --- | ---
                *renders* | `beautiful images` | ![random image](https://picsum.photos/seed/picsum/400/400 "Text 1")
                1 | 2 | 3
            
                > Blockquotes are very handy in email to emulate reply text.
                > This line is part of the same quote.
                """.trimIndent()
                )
            }
        }
    }
}


@Composable
fun SubscribedRecommendation(
    modifier: Modifier = Modifier
) {

    val localContext = LocalContext.current

    var location = LocationUtils().getLocation(localContext)

    var addressList = LocationUtils().getGeoFromLocation(localContext, location)
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(text = "Subscribed Screen")
        if (location != null) {
            Text(text = location.latitude.toString())
        }

        if (addressList.isNotEmpty()) {
            Text(text = addressList[0].featureName)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
fun DefaultRecommendation(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
    val recommendationUiState by mainViewModel.recommendationViewModel.uiState.collectAsState()

    if (recommendationUiState.blogList.isEmpty()) {
        mainViewModel.recommendationViewModel.updateBlogList()
    }

    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(1) }
    val refreshScope = rememberCoroutineScope()

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(1500)
        mainViewModel.recommendationViewModel.updateBlogList()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(mainViewModel.recommendationViewModel.uiState.value.blogList) {
                BlogsCard({
                    mainViewModel.BlogViewModel.updateBlog(it)
                    mainAppNavController.navigate("BlogScreen")
                },
                    it
                )
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}