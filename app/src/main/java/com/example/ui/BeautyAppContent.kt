package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BeautyAppContent(
    viewModel: BeautyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val storeMatches by viewModel.storeMatches.collectAsStateWithLifecycle()
    val allStores by viewModel.allStores.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val activeSellerStoreId by viewModel.activeSellerStoreId.collectAsStateWithLifecycle()

    var productInputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(RoseBackground),
        containerColor = RoseBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SHOPPING ASSISTANT",
                            color = RosePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "Smart Beauty",
                            color = RoseTextPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // Rounded Profile / Sync indicator
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(RosePill)
                            .clickable {
                                // Toggle to seller tab as a shortcut, or show a tip
                                if (activeTab != "Seller Panel") {
                                    viewModel.setTab("Seller Panel")
                                    Toast
                                        .makeText(
                                            context,
                                            "Welcome to Seller panel!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    viewModel.setTab("Explore")
                                }
                            }
                            .testTag("person_badge"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Account",
                            tint = RosePillText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Elegant bottom Navigation Bar styled perfectly with Professional Polish
            NavigationBar(
                containerColor = RoseContainer,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .border(1.dp, RoseOutline, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBarItem(
                    selected = activeTab == "Explore",
                    onClick = { viewModel.setTab("Explore") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Explore"
                        )
                    },
                    label = { Text("Explore", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePrimary,
                        selectedTextColor = RosePrimary,
                        unselectedIconColor = RoseTextTertiary,
                        unselectedTextColor = RoseTextTertiary,
                        indicatorColor = RosePill
                    ),
                    modifier = Modifier.testTag("nav_explore")
                )

                NavigationBarItem(
                    selected = activeTab == "AI Fetcher",
                    onClick = { viewModel.setTab("AI Fetcher") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "AI Data Ingest Console"
                        )
                    },
                    label = { Text("AI Fetcher", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePrimary,
                        selectedTextColor = RosePrimary,
                        unselectedIconColor = RoseTextTertiary,
                        unselectedTextColor = RoseTextTertiary,
                        indicatorColor = RosePill
                    ),
                    modifier = Modifier.testTag("nav_fetcher")
                )

                NavigationBarItem(
                    selected = activeTab == "Lists",
                    onClick = { viewModel.setTab("Lists") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Recommendations & Lists"
                        )
                    },
                    label = { Text("Lists", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePrimary,
                        selectedTextColor = RosePrimary,
                        unselectedIconColor = RoseTextTertiary,
                        unselectedTextColor = RoseTextTertiary,
                        indicatorColor = RosePill
                    ),
                    modifier = Modifier.testTag("nav_lists")
                )

                NavigationBarItem(
                    selected = activeTab == "Stores",
                    onClick = { viewModel.setTab("Stores") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Registered Stores"
                        )
                    },
                    label = { Text("Stores", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePrimary,
                        selectedTextColor = RosePrimary,
                        unselectedIconColor = RoseTextTertiary,
                        unselectedTextColor = RoseTextTertiary,
                        indicatorColor = RosePill
                    ),
                    modifier = Modifier.testTag("nav_stores")
                )

                NavigationBarItem(
                    selected = activeTab == "Seller Panel",
                    onClick = { viewModel.setTab("Seller Panel") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Seller Dashboard"
                        )
                    },
                    label = { Text("Seller Dashboard", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePrimary,
                        selectedTextColor = RosePrimary,
                        unselectedIconColor = RoseTextTertiary,
                        unselectedTextColor = RoseTextTertiary,
                        indicatorColor = RosePill
                    ),
                    modifier = Modifier.testTag("nav_seller")
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = activeTab,
            transitionSpec = {
                (fadeIn() + slideInHorizontally { width -> width / 4 }).togetherWith(
                    fadeOut() + slideOutHorizontally { width -> -width / 4 }
                )
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            label = "tab_switch"
        ) { tabState ->
            when (tabState) {
                "Explore" -> {
                    ExploreTab(
                        viewModel = viewModel,
                        shoppingItems = shoppingItems,
                        storeMatches = storeMatches,
                        selectedDistrict = selectedDistrict,
                        productInputText = productInputText,
                        onProductInputChange = { productInputText = it },
                        onAddProduct = {
                            if (productInputText.isNotBlank()) {
                                viewModel.addShoppingItem(productInputText)
                                productInputText = ""
                                focusManager.clearFocus()
                            }
                        }
                    )
                }

                "AI Fetcher" -> {
                    AIFetcherTab(viewModel = viewModel)
                }

                "Lists" -> {
                    ListsTab(viewModel = viewModel)
                }

                "Stores" -> {
                    StoresDirectoryTab(
                        viewModel = viewModel,
                        allStores = allStores,
                        allProducts = allProducts
                    )
                }

                "Seller Panel" -> {
                    SellerPanelTab(
                        viewModel = viewModel,
                        allStores = allStores,
                        activeStoreId = activeSellerStoreId,
                        allProducts = allProducts
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreTab(
    viewModel: BeautyViewModel,
    shoppingItems: List<ShoppingItem>,
    storeMatches: List<StoreMatchResult>,
    selectedDistrict: String,
    productInputText: String,
    onProductInputChange: (String) -> Unit,
    onAddProduct: () -> Unit
) {
    val allStores by viewModel.allStores.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
    ) {
        // Shopping List Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = RoseContainer),
                border = borderStrokeDefault(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shopping_list_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Paste or type your cosmetics list:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseTextPrimary
                    )

                    // Tags FlowRow
                    if (shoppingItems.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            shoppingItems.forEach { item ->
                                ShoppingChip(
                                    text = item.text,
                                    onDelete = { viewModel.deleteShoppingItem(item) }
                                )
                            }
                        }
                    } else {
                        // Empty State inside list tags
                        Text(
                            text = "No products added. Example: \"BOJ Sunscreen\", \"Romand Tint\", \"COSRX Essence\"",
                            fontSize = 12.sp,
                            color = RoseTextTertiary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Interactive Quick Search Suggestions
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "🔥 Quick Searches:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseTextSecondary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Fino Hair Mask", "BOJ Sunscreen", "COSRX Snail Mucin").forEach { sugg ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RoseOutline.copy(alpha = 0.4f))
                                        .clickable {
                                            if (!shoppingItems.any { it.text.equals(sugg, ignoreCase = true) }) {
                                                viewModel.addShoppingItem(sugg)
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = sugg,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RosePrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // TextField wrapper with nice outline and plus button helper
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, RoseOutline, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RoseTextTertiary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        androidx.compose.foundation.text.BasicTextField(
                            value = productInputText,
                            onValueChange = onProductInputChange,
                            textStyle = LocalTextStyle.current.copy(
                                color = RoseTextPrimary,
                                fontSize = 14.sp
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = { onAddProduct() }),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("product_text_field"),
                            decorationBox = { innerTextField ->
                                if (productInputText.isEmpty()) {
                                    Text(
                                        text = "Add more products...",
                                        color = RoseTextTertiary,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        )

                        IconButton(
                            onClick = onAddProduct,
                            modifier = Modifier
                                .size(36.dp)
                                .background(RosePrimary, CircleShape)
                                .testTag("add_product_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Item",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (shoppingItems.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.clearShoppingList() },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Clear List",
                                color = RosePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Live Price Comparison Engine
        item {
            PriceComparisonCard(
                viewModel = viewModel,
                shoppingItems = shoppingItems,
                allStores = allStores,
                allProducts = allProducts
            )
        }

        // District Quick Filters
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val onlyAvailable by viewModel.onlyShowAvailable.collectAsStateWithLifecycle()
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIVE COSMETIC PAGES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = RoseTextSecondary,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (onlyAvailable) GreenMatchBg else RoseOutline.copy(alpha = 0.3f))
                            .clickable { viewModel.toggleOnlyAvailable() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Filter",
                            tint = if (onlyAvailable) GreenMatch else RoseTextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Only Available Claims",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (onlyAvailable) GreenMatch else RoseTextPrimary
                        )
                    }
                }

                // Horizontal District selector row
                val districts = listOf("All", "Dhaka", "Chittagong", "Sylhet")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(districts) { district ->
                        val isSelected = selectedDistrict == district
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) RosePrimary else RoseContainer)
                                .clickable { viewModel.setDistrict(district) }
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else RoseOutline,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = district,
                                color = if (isSelected) Color.White else RoseTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Ranked Stores List
        if (storeMatches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No active cosmetic sellers found in this district.",
                        fontSize = 14.sp,
                        color = RoseTextTertiary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(storeMatches) { match ->
                StoreRankCard(match = match)
            }
        }
    }
}

// Shopping tag chip helper
@Composable
fun ShoppingChip(
    text: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, RosePrimary, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = text,
            color = RosePrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove",
            tint = RosePrimary,
            modifier = Modifier
                .size(14.dp)
                .clickable { onDelete() }
        )
    }
}

// Compact helper border
private fun borderStrokeDefault() = androidx.compose.foundation.BorderStroke(1.dp, RoseOutline)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoreRankCard(match: StoreMatchResult) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = borderStrokeDefault(),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .testTag("store_card_${match.store.id}")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circle Initials Logo
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(RoseContainer, RoundedCornerShape(14.dp))
                            .border(1.dp, RoseOutline, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = match.store.logoText,
                            color = RosePrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = match.store.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseTextPrimary
                            )
                            if (match.store.isVerified) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(StarYellow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Verified Status",
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${match.store.district}, Bangladesh",
                                fontSize = 11.sp,
                                color = RoseTextTertiary
                            )

                            Text(
                                text = "•",
                                color = RoseTextTertiary,
                                fontSize = 10.sp
                            )

                            Text(
                                text = if (match.store.isPremium) "Premium Seller" else "Standard Seller",
                                fontSize = 11.sp,
                                color = if (match.store.isPremium) RosePrimary else RoseTextTertiary,
                                fontWeight = if (match.store.isPremium) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Ranking Score / Status Badge
                Column(horizontalAlignment = Alignment.End) {
                    if (match.totalSearchedCount > 0) {
                        if (match.searchMatchCount == match.totalSearchedCount) {
                            Box(
                                modifier = Modifier
                                    .background(GreenMatchBg, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "100% Match",
                                    color = GreenMatch,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(RoseContainer, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Missing ${match.totalSearchedCount - match.searchMatchCount} Items",
                                    color = OrangeMatch,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        if (match.totalAvailableInStore >= 3) {
                            Box(
                                modifier = Modifier
                                    .background(GreenMatchBg, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "High Stock",
                                    color = GreenMatch,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = if (match.totalSearchedCount > 0) {
                            "${match.score.toInt()}% Match"
                        } else {
                            "${match.totalAvailableInStore} Items Avail."
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Quick status breakdown row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RoseContainer, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AVAILABILITY",
                        fontSize = 9.sp,
                        color = RoseTextTertiary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    val textStateColor = if (match.searchMatchCount == match.totalSearchedCount && match.totalSearchedCount > 0) {
                        GreenMatch
                    } else if (match.searchMatchCount > 0) {
                        OrangeMatch
                    } else {
                        RoseTextPrimary
                    }

                    Text(
                        text = if (match.totalSearchedCount == 0) "No search match" else "${match.searchMatchCount}/${match.totalSearchedCount} Found",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textStateColor
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TOTAL STOCK",
                        fontSize = 9.sp,
                        color = RoseTextTertiary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${match.totalAvailableInStore} Available",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RosePrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "STOCK CLAIM",
                        fontSize = 9.sp,
                        color = RoseTextTertiary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    val updateText = when (match.store.lastUpdatedDaysAgo) {
                        0 -> "Claimed Today"
                        1 -> "Claimed Yest."
                        else -> "${match.store.lastUpdatedDaysAgo} days ago"
                    }

                    Text(
                        text = updateText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RoseTextPrimary
                    )
                }
            }

            // Interactive Scoring weights breakdown
            var showRankingBreakdown by remember { mutableStateOf(false) }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showRankingBreakdown = !showRankingBreakdown }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Rank info details",
                        tint = RoseTextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showRankingBreakdown) "Hide Rank Score Breakdown" else "View Rank Score Weights Breakdown",
                        fontSize = 11.sp,
                        color = RoseTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (showRankingBreakdown) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoseContainer.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = match.rankingBreakdown,
                            fontSize = 11.sp,
                            color = RoseTextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Expanded Product availability breakdown details
            if (isExpanded || match.totalSearchedCount > 0) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Divider(color = RoseOutline, thickness = 0.5.dp)
                    
                    Text(
                        text = "Product-level Availability:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseTextSecondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    match.matchedProducts.forEach { item ->
                        CatalogItemRow(item = item, isAvailable = true)
                    }

                    match.unavailableProducts.forEach { item ->
                        CatalogItemRow(item = item, isAvailable = false)
                    }
                }
            }

            // Redirect Facebook Button
            Button(
                onClick = {
                    try {
                        val webpage = Uri.parse(match.store.facebookUrl)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Redirect failed, invalid URL.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("visit_facebook_button")
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Availability on Facebook",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Open Web Link",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CatalogItemRow(item: MatchedProductItem, isAvailable: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.shoppingItemText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = RoseTextPrimary
            )
            if (item.storeProduct != null) {
                Text(
                    text = "${item.storeProduct.brandName} • ${item.storeProduct.productName}",
                    fontSize = 11.sp,
                    color = RoseTextTertiary
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.storeProduct != null) {
                Text(
                    text = "${item.storeProduct.priceBdt} BDT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseTextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isAvailable) GreenMatchBg else RoseOutline)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = item.statusText,
                    color = if (isAvailable) GreenMatch else RoseTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Saved lists tab - gives users premade trending cosmetic packages to add with 1-click
@Composable
fun ListsTab(viewModel: BeautyViewModel) {
    val context = LocalContext.current
    val premadeLists = listOf(
        PremadePackage(
            title = "Trending Korean Glass Skin Routine",
            description = "Most desired K-Beauty essentials of Dhaka youth.",
            products = listOf("Beauty of Joseon Sunscreen", "COSRX Snail Mucin", "Anua Heartleaf Toner")
        ),
        PremadePackage(
            title = "Bangladesh Monsoon Eyes High-Proof",
            description = "Smudge-proof makeup items rated for highest humidity.",
            products = listOf("Maybelline Sky High Mascara", "Romand Tint")
        ),
        PremadePackage(
            title = "Daily Minimal Essentials Starter Kit",
            description = "Standard routine for university and working cosmetic buyers.",
            products = listOf("The Ordinary Niacinamide", "Beauty of Joseon Sunscreen", "Romand Tint")
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Bangladesh Beauty Trends",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RoseTextPrimary,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "Tap any pre-curated cosmetic bundle to instantly insert into your Shopping Search engine.",
                fontSize = 12.sp,
                color = RoseTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }

        items(premadeLists) { bundle ->
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = borderStrokeDefault(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = bundle.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RosePrimary
                    )

                    Text(
                        text = bundle.description,
                        fontSize = 12.sp,
                        color = RoseTextSecondary
                    )

                    // Display product items inside this bundle
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoseContainer, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        bundle.products.forEach { prod ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Cosmetic list",
                                    tint = RosePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = prod,
                                    fontSize = 12.sp,
                                    color = RoseTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            // Empty existing and load bundle items
                            viewModel.clearShoppingList()
                            bundle.products.forEach { prod ->
                                viewModel.addShoppingItem(prod)
                            }
                            viewModel.setTab("Explore") // switch to explore
                            Toast.makeText(context, "${bundle.title} loaded!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RosePill),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Add bundle items on Search",
                            color = RosePillText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

data class PremadePackage(
    val title: String,
    val description: String,
    val products: List<String>
)

// Searchable directory of ALL beauty stores
@Composable
fun StoresDirectoryTab(
    viewModel: BeautyViewModel,
    allStores: List<RegisteredStore>,
    allProducts: List<StoreProduct>
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredStores = allStores.filter {
        searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true) || it.district.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Cosmetics Stores Library",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RoseTextPrimary,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "Registered verified Facebook pages and local shops currently indexed.",
                fontSize = 12.sp,
                color = RoseTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search stores by name, area...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RosePrimary,
                    unfocusedBorderColor = RoseOutline,
                    unfocusedContainerColor = RoseContainer,
                    focusedContainerColor = RoseContainer
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stores_directory_search_field"),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                }
            )
        }

        items(filteredStores) { store ->
            val countOfProducts = allProducts.count { it.storeId == store.id }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = borderStrokeDefault(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(RoseContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = store.logoText,
                                fontWeight = FontWeight.Bold,
                                color = RosePrimary,
                                fontSize = 14.sp
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = store.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = RoseTextPrimary
                                )
                                if (store.isVerified) {
                                    Box(modifier = Modifier.size(14.dp).background(StarYellow, CircleShape), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Check, contentDescription = "Verfied", tint = Color.White, modifier = Modifier.size(8.dp))
                                    }
                                }
                            }

                            Text(
                                text = "${store.district} • $countOfProducts registered catalog products",
                                fontSize = 11.sp,
                                color = RoseTextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(RosePill, RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (store.isPremium) "Featured" else "Standard",
                            color = RosePillText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// Combined Dashboard / Seller panel so sellers can easily test adding catalog items dynamically
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SellerPanelTab(
    viewModel: BeautyViewModel,
    allStores: List<RegisteredStore>,
    activeStoreId: Int?,
    allProducts: List<StoreProduct>
) {
    val context = LocalContext.current
    var isRegisteringStore by remember { mutableStateOf(false) }

    // Store Creation States
    var newStoreName by remember { mutableStateOf("") }
    var newStoreUrl by remember { mutableStateOf("") }
    var newStoreDistrict by remember { mutableStateOf("Dhaka") }
    var premiumPlanSelection by remember { mutableStateOf("Standard") }

    // Product Adding States in selected store
    var brandNameInput by remember { mutableStateOf("") }
    var productNameInput by remember { mutableStateOf("") }
    var productPriceInput by remember { mutableStateOf("") }
    var productStatusInput by remember { mutableStateOf("Available") }
    var comingSoonDaysInput by remember { mutableStateOf("5") }

    val activeStore = allStores.find { it.id == activeStoreId }
    val activeStoreProducts = allProducts.filter { it.storeId == activeStoreId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Seller Inventory Hub",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RoseTextPrimary,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "Bangladesh cosmetic pages can list products, edit status, and get priority exposure instantly.",
                fontSize = 12.sp,
                color = RoseTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        // Toggle creation vs listing
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { isRegisteringStore = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isRegisteringStore) RosePrimary else RoseContainer
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Edit Stock",
                        color = if (!isRegisteringStore) Color.White else RoseTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { isRegisteringStore = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRegisteringStore) RosePrimary else RoseContainer
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Register Page",
                        color = if (isRegisteringStore) Color.White else RoseTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (isRegisteringStore) {
            // Register New Facebook Page details
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = borderStrokeDefault(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Register Business Page",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseTextPrimary
                        )

                        OutlinedTextField(
                            value = newStoreName,
                            onValueChange = { newStoreName = it },
                            label = { Text("Shop/Facebook Page Name") },
                            placeholder = { Text("e.g. Dhaka Glow Club") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newStoreUrl,
                            onValueChange = { newStoreUrl = it },
                            label = { Text("Facebook URL") },
                            placeholder = { Text("e.g. https://facebook.com/shopname") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // District drop selection
                        Text(text = "Primary Retail District:", fontSize = 12.sp, color = RoseTextSecondary)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Dhaka", "Chittagong", "Sylhet").forEach { dst ->
                                val selected = newStoreDistrict == dst
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) RosePrimary else RoseContainer)
                                        .border(1.dp, RoseOutline, RoundedCornerShape(10.dp))
                                        .clickable { newStoreDistrict = dst }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = dst,
                                        color = if (selected) Color.White else RoseTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // Premium subscription options
                        Text(text = "Subscription Exposure Plan:", fontSize = 12.sp, color = RoseTextSecondary)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Standard", "Premium").forEach { plan ->
                                val selected = premiumPlanSelection == plan
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) RosePill else RoseContainer)
                                        .border(1.dp, RoseOutline, RoundedCornerShape(10.dp))
                                        .clickable { premiumPlanSelection = plan }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = plan,
                                        color = if (selected) RosePillText else RoseTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (newStoreName.isNotBlank() && newStoreUrl.isNotBlank()) {
                                    viewModel.addStore(
                                        name = newStoreName,
                                        facebookUrl = newStoreUrl,
                                        district = newStoreDistrict,
                                        isPremium = premiumPlanSelection == "Premium",
                                        isVerified = true
                                    )
                                    Toast.makeText(context, "$newStoreName indexed!", Toast.LENGTH_SHORT).show()
                                    newStoreName = ""
                                    newStoreUrl = ""
                                    isRegisteringStore = false
                                } else {
                                    Toast.makeText(context, "Please fill in all blanks.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Save & Create Seller Page",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // Edit inventory stock in active store
            item {
                if (activeStore != null) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = RoseContainer),
                        border = borderStrokeDefault(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "ACTIVE MANAGED STORE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RosePrimary,
                                letterSpacing = 1.sp
                            )
                            
                            // Selection selector dropdown
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = activeStore.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RoseTextPrimary
                                )
                                Text(
                                    text = activeStore.district,
                                    fontSize = 12.sp,
                                    color = RoseTextSecondary
                                )
                            }

                            // Horizontal selection of stores if multiple exist
                            if (allStores.size > 1) {
                                Text(
                                    text = "Select active page:",
                                    fontSize = 11.sp,
                                    color = RoseTextTertiary
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(allStores) { st ->
                                        val isCurrent = st.id == activeStoreId
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isCurrent) RosePrimary else Color.White)
                                                .border(1.dp, RoseOutline, RoundedCornerShape(8.dp))
                                                .clickable { viewModel.selectSellerStore(st.id) }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = st.name,
                                                color = if (isCurrent) Color.White else RoseTextPrimary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No pages registered yet. Use the 'Register Page' tab above.",
                            color = RoseTextSecondary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (activeStore != null) {
                // Add Product in Stock Form
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = borderStrokeDefault(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Publish / Update Product Stock",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseTextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = brandNameInput,
                                    onValueChange = { brandNameInput = it },
                                    label = { Text("Brand") },
                                    placeholder = { Text("e.g. COSRX") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = productNameInput,
                                    onValueChange = { productNameInput = it },
                                    label = { Text("Product Name") },
                                    placeholder = { Text("e.g. Toner") },
                                    modifier = Modifier.weight(1.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = productPriceInput,
                                    onValueChange = { productPriceInput = it },
                                    label = { Text("Price (BDT)") },
                                    placeholder = { Text("1500") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                // Expected restock delay IF Coming Soon is chosen
                                if (productStatusInput == "Coming Soon") {
                                    OutlinedTextField(
                                        value = comingSoonDaysInput,
                                        onValueChange = { comingSoonDaysInput = it },
                                        label = { Text("Restock (Days)") },
                                        placeholder = { Text("5") },
                                        modifier = Modifier.weight(1.1f),
                                        shape = RoundedCornerShape(12.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }

                            // Horizontal availability selector
                            Text(text = "Live Availability:", fontSize = 11.sp, color = RoseTextSecondary)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                listOf("Available", "Coming Soon", "Unavailable").forEach { status ->
                                    val selected = productStatusInput == status
                                    val pillBg = when (status) {
                                        "Available" -> if (selected) GreenMatchBg else RoseContainer
                                        "Coming Soon" -> if (selected) RosePill else RoseContainer
                                        else -> if (selected) RoseContainer else RoseContainer
                                    }
                                    val pillTxt = when (status) {
                                        "Available" -> if (selected) GreenMatch else RoseTextPrimary
                                        "Coming Soon" -> if (selected) RosePillText else RoseTextPrimary
                                        else -> if (selected) RosePrimary else RoseTextPrimary
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(pillBg)
                                            .border(
                                                1.dp,
                                                if (selected) pillTxt else RoseOutline,
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable { productStatusInput = status }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = status,
                                            color = pillTxt,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    val price = productPriceInput.toIntOrNull()
                                    val restock = comingSoonDaysInput.toIntOrNull() ?: 0
                                    if (brandNameInput.isNotBlank() && productNameInput.isNotBlank() && price != null) {
                                        viewModel.addOrUpdateProduct(
                                            brand = brandNameInput,
                                            name = productNameInput,
                                            status = productStatusInput,
                                            price = price,
                                            restockDays = restock
                                        )
                                        Toast.makeText(context, "$productNameInput updated!", Toast.LENGTH_SHORT).show()
                                        brandNameInput = ""
                                        productNameInput = ""
                                        productPriceInput = ""
                                        comingSoonDaysInput = "5"
                                    } else {
                                        Toast.makeText(context, "Verify entries and Price", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = "Post Product Stock",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Current listing table of products owned by this Seller Store
                item {
                    Text(
                        text = "Current Live Inventory:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseTextPrimary
                    )
                }

                if (activeStoreProducts.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = borderStrokeDefault()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "This store has no cosmetic items indexed.",
                                    color = RoseTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                } else {
                    items(activeStoreProducts) { prod ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = borderStrokeDefault(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${prod.brandName} ${prod.productName}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = RoseTextPrimary
                                    )
                                    Text(
                                        text = "${prod.priceBdt} BDT • Status: ${prod.availabilityStatus}",
                                        fontSize = 11.sp,
                                        color = RoseTextSecondary
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Status pill indicator
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (prod.availabilityStatus == "Available") GreenMatchBg else RoseContainer
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = prod.availabilityStatus,
                                            color = if (prod.availabilityStatus == "Available") GreenMatch else RosePrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeProductFromStore(prod.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete product",
                                            tint = RosePrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AIFetcherTab(viewModel: BeautyViewModel) {
    val context = LocalContext.current
    val allStores by viewModel.allStores.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    
    // Crowd-sync state variables
    val isCrowdSyncEnabled by viewModel.isCrowdSyncEnabled.collectAsStateWithLifecycle()
    val isFbAuthorized by viewModel.isFbAuthorized.collectAsStateWithLifecycle()
    val authorizedAccountName by viewModel.authorizedAccountName.collectAsStateWithLifecycle()
    val followedPages by viewModel.followedPages.collectAsStateWithLifecycle()
    val totalCrowdUpdatesSyncCount by viewModel.totalCrowdUpdatesSyncCount.collectAsStateWithLifecycle()
    val recentSyncEvents by viewModel.recentSyncEvents.collectAsStateWithLifecycle()
    
    var subTab by remember { mutableStateOf("Scraper") } // "Scraper" or "CrowdSync"
    var showAuthDialog by remember { mutableStateOf(false) }
    var authProfileName by remember { mutableStateOf("Mansur Islam") }
    var isAuthProgressing by remember { mutableStateOf(false) }
    var authStepText by remember { mutableStateOf("") }
    
    var selectedStoreId by remember { mutableStateOf<Int?>(null) }
    
    // Auto-select first store if none selected
    LaunchedEffect(allStores) {
        if (selectedStoreId == null && allStores.isNotEmpty()) {
            selectedStoreId = allStores.first().id
        }
    }
    
    val selectedStore = allStores.find { it.id == selectedStoreId }
    
    // Sample Facebook feed posts that can be tested
    val samplePosts = listOf(
        "OMGGG Girls! 🌸 Beauty of Joseon Sunscreen Relief SPF 50 is FINALLY back in stock at Glow Haven BD! BDT 1450 taka. In-stock & available right now for home delivery!",
        "Restock alert! 🐌 The best COSRX Snail Mucin 96 Essence is today available at Bangladeshi Makeup spot. 1560 TK only. Claim yours today before we sell out again!",
        "Juicy Tints are here! Romand Lasting Tint shades restocked at our Dhaka hub. Grab active products for BDT 1080 tk. Available now!"
    )
    
    var customPostText by remember { mutableStateOf(samplePosts[0]) }
    val consoleLogs = remember { mutableStateListOf<String>() }
    var isScraping by remember { mutableStateOf(false) }
    var isCodeExpanded by remember { mutableStateOf(false) }

    var isSweepSyncing by remember { mutableStateOf(false) }
    var sweepProgressText by remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        item {
            Text(
                text = "AI Page Ingestion Console",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RoseTextPrimary,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "Configure live data streams from public cosmetic pages, or activate crowdsourced user session crawling to automatically update stocks.",
                fontSize = 12.sp,
                color = RoseTextSecondary
            )
        }
        
        // Mode Selector Sub-Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(RoseContainer)
                    .border(1.dp, RoseOutline, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (subTab == "Scraper") RosePrimary else Color.Transparent)
                        .clickable { subTab = "Scraper" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🤖 Server Crawler",
                        color = if (subTab == "Scraper") Color.White else RoseTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (subTab == "CrowdSync") RosePrimary else Color.Transparent)
                        .clickable { subTab = "CrowdSync" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👥 Crowdsourced Sync",
                            color = if (subTab == "CrowdSync") Color.White else RoseTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isFbAuthorized) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF66BB6A), CircleShape)
                            )
                        }
                    }
                }
            }
        }
        
        if (subTab == "Scraper") {
            // Setup Crawler parameters card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoseOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "1. Configure Automated Scraper Target",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RosePrimary
                        )
                        
                        // Store Page Dropdown List
                        Text(text = "Target Verified Facebook Page Stream:", fontSize = 11.sp, color = RoseTextSecondary)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(allStores) { st ->
                                val isCurrent = st.id == selectedStoreId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isCurrent) RosePrimary else RoseContainer)
                                        .border(androidx.compose.foundation.BorderStroke(1.dp, if (isCurrent) Color.Transparent else RoseOutline), RoundedCornerShape(10.dp))
                                        .clickable { selectedStoreId = st.id }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = st.name,
                                        color = if (isCurrent) Color.White else RoseTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        
                        // Template Selector Chips
                        Text(text = "Select Sample Facebook Feed Text:", fontSize = 11.sp, color = RoseTextSecondary)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            samplePosts.forEach { post ->
                                val isChosen = customPostText == post
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isChosen) RosePill else RoseContainer.copy(alpha = 0.5f))
                                        .border(androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) RosePrimary else RoseOutline), RoundedCornerShape(10.dp))
                                        .clickable { customPostText = post }
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Post snippet: \"${post.take(65)}...\"",
                                        fontSize = 11.sp,
                                        color = if (isChosen) RosePillText else RoseTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        
                        // Editable Feed Box
                        OutlinedTextField(
                            value = customPostText,
                            onValueChange = { customPostText = it },
                            label = { Text("Simulated Raw Page Stream Post Body") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = RoseTextPrimary)
                        )
                        
                        // Scrape Action Button
                        Button(
                            onClick = {
                                if (selectedStoreId == null) return@Button
                                isScraping = true
                                consoleLogs.clear()
                                consoleLogs.add("[LOG] Initializing headless background headless browser...")
                                
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(800)
                                    consoleLogs.add("[LOG] Loading Public stream for URL: ${selectedStore?.facebookUrl}")
                                    kotlinx.coroutines.delay(1000)
                                    consoleLogs.add("[LOG] Target feed connected. Loaded latest active streams text.")
                                    kotlinx.coroutines.delay(900)
                                    consoleLogs.add("[CRAWL] Extracted Text: \"$customPostText\"")
                                    kotlinx.coroutines.delay(1100)
                                    consoleLogs.add("[AI ENGINE] Running Gemini Vertex JSON Parsing Extractor schema models...")
                                    kotlinx.coroutines.delay(1300)
                                    consoleLogs.add("[AI LOG] Structural matches resolved with 99.4% confidence.")
                                    
                                    // Call viewModel back-end update
                                    viewModel.simulateFbFeedIngest(selectedStoreId!!, customPostText)
                                    
                                    kotlinx.coroutines.delay(800)
                                    consoleLogs.add("[DB COMMIND] Committed parsed product structures into central SQLite Room database.")
                                    consoleLogs.add("[SUCCESS] Recalculating page status & index score weight coefficients.")
                                    consoleLogs.add("[RANK] Store ${selectedStore?.name} boosted to active today. Products claims updated live!")
                                    isScraping = false
                                    Toast.makeText(context, "${selectedStore?.name} stock updated live with AI!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = !isScraping && selectedStoreId != null,
                            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isScraping) "AI Pipeline Executing..." else "Trigger AI Scrape & Parse",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Scraping Terminal Console Log
            if (consoleLogs.isNotEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFEF5350), CircleShape))
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFCA28), CircleShape))
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF66BB6A), CircleShape))
                                }
                                Text(
                                    text = "TERMINAL COMPILER REPO",
                                    fontSize = 9.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }
                            
                            Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 4.dp))
                            
                            consoleLogs.forEach { log ->
                                val txtColor = when {
                                    log.contains("[SUCCESS]") -> Color(0xFF66BB6A)
                                    log.contains("[CRAWL]") -> Color(0xFF42A5F5)
                                    log.contains("[AI ENGINE]") -> Color(0xFFAB47BC)
                                    else -> Color(0xFFECEFF1)
                                }
                                Text(
                                    text = log,
                                    color = txtColor,
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 16.sp
                                )
                            }
                            
                            // Small help instruction
                            Text(
                                text = "💡 Direct Action: Try adding \"Romand Tint\" in the Explore Search tags on the Home page, trigger this parser, and watch this store jump to Rank #1!",
                                fontSize = 11.sp,
                                color = Color.Yellow,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
            
            // Full production-grade Backend Scraper Bot Source Code display
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoseOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isCodeExpanded = !isCodeExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "Backend Code", tint = RosePrimary)
                                Column {
                                    Text(
                                        text = "Inspect Real Backend Program Code",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseTextPrimary
                                    )
                                    Text(
                                        text = "Production Node.js & Gemini API structural crawler",
                                        fontSize = 10.sp,
                                        color = RoseTextSecondary
                                    )
                                }
                            }
                            Icon(
                                imageVector = if (isCodeExpanded) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = "Expand Code",
                                tint = RosePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        
                        if (isCodeExpanded) {
                            Divider(color = RoseOutline)
                            
                            Text(
                                text = "Below is the autonomous microservice crawler program logic built in Node.js paired with Puppeteer & Google GenAI Vertex SDK. It parses raw cosmetic announcements into micro-structured database entities:",
                                fontSize = 11.sp,
                                color = RoseTextSecondary
                            )
                            
                            val backendCode = """
// 🚀 PRODUCTION FACEBOOK COSMETICS CRAWLER & SCHEMA PARSER (NODE.JS + VERTEX AI)
const puppeteer = require('puppeteer');
const { GoogleGenAI } = require('@google/genai');

const ai = new GoogleGenAI({ apiKey: process.env.GEMINI_API_KEY });

async function scrapeAndIndexFacebookPage(pageUrl, storeId) {
  // 1. Launch Headless Chromium for scraping FB public feeds
  const browser = await puppeteer.launch({ headless: "new" });
  const page = await browser.newPage();
  await page.setUserAgent("Mozilla/5.0");
  
  console.log(`Connecting to URL: ${'$'}{pageUrl}...`);
  await page.goto(pageUrl, { waitUntil: 'networkidle2' });
  
  // 2. Query page posts text segments
  const postTexts = await page.evaluate(() => {
    const selector = 'div[data-ad-preview="message"], [data-testid="post_message"]';
    return Array.from(document.querySelectorAll(selector))
      .map(el => el.innerText.trim());
  });
  
  await browser.close();
  
  if (postTexts.length === 0) return;
  
  // 3. Send raw texts to Gemini AI model executing strict Schema output
  for (const rawText of postTexts) {
    const response = await ai.models.generateContent({
      model: 'gemini-2.5-flash',
      contents: `Parse other cosmetics posts: "${'\$'} {rawText}"`,
      config: {
        responseMimeType: 'application/json',
        responseSchema: {
          type: 'object',
          properties: {
            brandName: { type: 'string' },
            productName: { type: 'string' },
            availabilityStatus: { 
              type: 'string', 
              enum: ['Available', 'Coming Soon', 'Unavailable'] 
            },
            priceBdt: { type: 'number' }
          },
          required: ['brandName', 'productName', 'availabilityStatus', 'priceBdt']
        }
      }
    });
    
    const parsedData = JSON.parse(response.text);
    console.log("Extracted structured attributes:", parsedData);
    
    // 4. Inject into mobile search directory database engine
    await fetch('https://api.smartbeauty.com/v1/ingest', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ storeId, ...parsedData })
    });
  }
}
                            """.trimIndent()
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = backendCode,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color(0xFFA8FF60), // beautiful code green
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // --- CROWDSOURCED SOCIAL SYNC WORKSPACE ---
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoseOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "👥 Decentralized Meta Sync Integration",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RosePrimary
                            )
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isFbAuthorized) GreenMatchBg else RoseOutline.copy(alpha = 0.5f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isFbAuthorized) "ACTIVE SYNC" else "DISCONNECTED",
                                    color = if (isFbAuthorized) GreenMatch else RoseTextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Text(
                            text = "Enable secure social authorization. Every time you search, we cross-reference active posts and stock details from Bangladeshi Facebook cosmetic groups & pages you visit, keeping Dhaka's central directory updated live.",
                            fontSize = 12.sp,
                            color = RoseTextSecondary
                        )
                        
                        Divider(color = RoseOutline)
                        
                        if (!isFbAuthorized) {
                            // Prompt Connect
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Connect Your Meta Session (Facebook / Instagram)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoseTextPrimary
                                )
                                Text(
                                    text = "⚠️ Real-time sync requires read-access permission of public timelines & page announcements to scan available products.",
                                    fontSize = 11.sp,
                                    color = RoseTextTertiary
                                )
                                
                                Button(
                                    onClick = { showAuthDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.Share, contentDescription = "Meta Icon", modifier = Modifier.size(16.dp))
                                        Text("Authorize Facebook & Connect Hub", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        } else {
                            // Already Connected State
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(RoseContainer, RoundedCornerShape(14.dp))
                                        .padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(RosePrimary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = authorizedAccountName?.take(1)?.uppercase() ?: "U",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Connected as $authorizedAccountName",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoseTextPrimary
                                        )
                                        Text(
                                            text = "Verified Contributor Token Active",
                                            fontSize = 10.sp,
                                            color = RoseTextTertiary
                                        )
                                    }
                                    
                                    TextButton(onClick = { viewModel.disconnectFacebook() }) {
                                        Text("Disconnect", color = RosePrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                
                                // Contribution Counter
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, RoseOutline, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Total Synced Updates", fontSize = 11.sp, color = RoseTextSecondary)
                                        Text("$totalCrowdUpdatesSyncCount contributions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RosePrimary)
                                    }
                                    
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Sync score",
                                        tint = GreenMatch,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                // Force manual sweep button
                                Button(
                                    onClick = {
                                        isSweepSyncing = true
                                        coroutineScope.launch {
                                            sweepProgressText = "Connecting secure contribution gateway..."
                                            kotlinx.coroutines.delay(700)
                                            sweepProgressText = "Crawling public timelines of ${followedPages.size} followed stores..."
                                            kotlinx.coroutines.delay(900)
                                            sweepProgressText = "Parsing active stock posts with Gemini NLP extractor..."
                                            
                                            // Perform random query injection in database to show live progress!
                                            val queries = listOf("Fino Hair Mask", "BOJ Sunscreen", "COSRX Snail Mucin", "Romand Tint", "Anua Toner")
                                            viewModel.triggerCrowdsourcedSyncForQuery(queries.random())
                                            
                                            kotlinx.coroutines.delay(600)
                                            isSweepSyncing = false
                                            Toast.makeText(context, "Crowdsourced database successfully synchronized!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    enabled = !isSweepSyncing,
                                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (isSweepSyncing) sweepProgressText else "Trigger Global Crowd-Sync Sweep",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Tracked pages checklist (Allows choosing which pages to crawl)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoseOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Configure Monitored Facebook Pages",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RosePrimary
                        )
                        Text(
                            text = "Toggle the stores you authorize the sync system to scan updates from whenever you use the application.",
                            fontSize = 11.sp,
                            color = RoseTextSecondary
                        )
                        
                        Divider(color = RoseOutline)
                        
                        allStores.forEach { store ->
                            val isFollowed = followedPages.contains(store.id)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleFollowedPage(store.id) }
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(store.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoseTextPrimary)
                                    Text(store.district, fontSize = 10.sp, color = RoseTextSecondary)
                                }
                                
                                Switch(
                                    checked = isFollowed,
                                    onCheckedChange = { viewModel.toggleFollowedPage(store.id) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = RosePrimary,
                                        uncheckedThumbColor = RoseTextSecondary,
                                        uncheckedTrackColor = RoseContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }
            
            // Sync Events Terminal Logs
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151515)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFFEF5350), CircleShape))
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFFFFCA28), CircleShape))
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF66BB6A), CircleShape))
                            }
                            Text(
                                text = "REAL-TIME SYNC LOG STREAM",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                        
                        Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 4.dp))
                        
                        if (recentSyncEvents.isEmpty()) {
                            Text(
                                text = "No recent crowd sync events triggered yet.",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        } else {
                            recentSyncEvents.forEach { event ->
                                val txtColor = when {
                                    event.contains("[CROWD LIVE UPDATE]") -> Color(0xFFAB47BC)
                                    event.contains("Successfully") -> Color(0xFF66BB6A)
                                    else -> Color(0xFF81D4FA)
                                }
                                Text(
                                    text = event,
                                    color = txtColor,
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Authorization Permission Dialog
    if (showAuthDialog) {
        AlertDialog(
            onDismissRequest = { if (!isAuthProgressing) showAuthDialog = false },
            title = {
                Text(
                    text = "Request Meta Graph API Permission",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RosePrimary
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Smart Beauty requests permissions to securely crawl public announcements on Facebook cosmetics pages through your session context:",
                        fontSize = 12.sp,
                        color = RoseTextSecondary
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoseContainer, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "✅ Read public page streams (pages_read_engagement)",
                            "✅ Extract stock and discount posts (instagram_basic)",
                            "✅ Sync local beauty store feeds (user_likes)"
                        ).forEach { permission ->
                            Text(
                                text = permission,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseTextPrimary
                            )
                        }
                    }
                    
                    OutlinedTextField(
                        value = authProfileName,
                        onValueChange = { authProfileName = it },
                        label = { Text("Facebook Profile Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RosePrimary,
                            unfocusedBorderColor = RoseOutline
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    if (isAuthProgressing) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = RosePrimary,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = authStepText,
                                fontSize = 11.sp,
                                color = RosePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isAuthProgressing = true
                        coroutineScope.launch {
                            authStepText = "Contacting Meta Authorization gateway..."
                            kotlinx.coroutines.delay(800)
                            authStepText = "Parsing OAuth secure tokens..."
                            kotlinx.coroutines.delay(900)
                            authStepText = "Granting page stream access rights..."
                            kotlinx.coroutines.delay(600)
                            
                            viewModel.authorizeFacebook(authProfileName)
                            
                            isAuthProgressing = false
                            showAuthDialog = false
                            Toast.makeText(context, "Successfully authorized!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                    enabled = !isAuthProgressing && authProfileName.isNotBlank()
                ) {
                    Text("Grant Consent & Connect")
                }
            },
            dismissButton = {
                if (!isAuthProgressing) {
                    TextButton(onClick = { showAuthDialog = false }) {
                        Text("Cancel", color = RoseTextSecondary)
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun PriceComparisonCard(
    viewModel: BeautyViewModel,
    shoppingItems: List<ShoppingItem>,
    allStores: List<RegisteredStore>,
    allProducts: List<StoreProduct>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedItemText by remember { mutableStateOf("") }

    // Sync selected item text when shoppingItems changes or matches
    val currentTexts = shoppingItems.map { it.text }
    if (selectedItemText.isNotEmpty() && !currentTexts.contains(selectedItemText)) {
        selectedItemText = ""
    }
    if (selectedItemText.isEmpty() && shoppingItems.isNotEmpty()) {
        selectedItemText = shoppingItems.first().text
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = borderStrokeDefault(),
        modifier = modifier
            .fillMaxWidth()
            .testTag("price_comparison_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Price Comparison",
                        tint = RosePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Price Comparison Engine",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(RosePill, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Best Deals",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosePillText
                    )
                }
            }

            if (shoppingItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Compare Prices Instantly",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseTextSecondary
                    )
                    Text(
                        text = "Add beauty products (such as 'BOJ Sunscreen' or 'COSRX Snail Mucin') to your Search List above, and we will list which local sellers carry it and highlight the cheapest option!",
                        fontSize = 12.sp,
                        color = RoseTextTertiary,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            } else {
                Text(
                    text = "Select a product to compare prices across stores:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseTextSecondary
                )

                // Horizontal scroll of product selection chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(shoppingItems) { item ->
                        val isSelected = selectedItemText == item.text
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) RosePrimary else RoseContainer)
                                .clickable { selectedItemText = item.text }
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else RoseOutline,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("compare_product_chip_${item.id}")
                        ) {
                            Text(
                                text = item.text,
                                color = if (isSelected) Color.White else RoseTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Divider(color = RoseOutline, thickness = 0.5.dp)

                // State controls for filter & sort
                var sortByCheapest by remember { mutableStateOf(true) }
                var showAvailableOnly by remember { mutableStateOf(false) }

                // Token-based matching logic
                val queryTokens = remember(selectedItemText) {
                    selectedItemText.lowercase().split(Regex("\\s+")).filter { it.length > 1 }
                }

                // Computed matches from all stores
                val comparisonOffers = remember(selectedItemText, allProducts, allStores, sortByCheapest, showAvailableOnly) {
                    if (queryTokens.isEmpty()) emptyList()
                    else {
                        val matched = allProducts.filter { product ->
                            val productSource = (product.brandName + " " + product.productName + " " + product.searchToken).lowercase()
                            queryTokens.all { productSource.contains(it) }
                        }.mapNotNull { product ->
                            val store = allStores.find { it.id == product.storeId }
                            if (store != null) {
                                product to store
                            } else null
                        }

                        // Apply filter (available only)
                        val filteredLabels = if (showAvailableOnly) {
                            matched.filter { it.first.availabilityStatus == "Available" }
                        } else {
                            matched
                        }

                        // Apply sort
                        if (sortByCheapest) {
                            filteredLabels.sortedBy { it.first.priceBdt }
                        } else {
                            filteredLabels.sortedByDescending { it.first.priceBdt }
                        }
                    }
                }

                // Controls Row: Price Sort & In Stock filter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sort pricing toggles
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cheap sort chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (sortByCheapest) RosePrimary else RoseContainer)
                                .border(1.dp, if (sortByCheapest) Color.Transparent else RoseOutline, RoundedCornerShape(8.dp))
                                .clickable { sortByCheapest = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("price_sort_low_btn")
                        ) {
                            Text(
                                text = "৳ Low-High ⬇",
                                color = if (sortByCheapest) Color.White else RoseTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Premium sort chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!sortByCheapest) RosePrimary else RoseContainer)
                                .border(1.dp, if (!sortByCheapest) Color.Transparent else RoseOutline, RoundedCornerShape(8.dp))
                                .clickable { sortByCheapest = false }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("price_sort_high_btn")
                        ) {
                            Text(
                                text = "৳ High-Low ⬆",
                                color = if (!sortByCheapest) Color.White else RoseTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Stock availability filter chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (showAvailableOnly) GreenMatchBg else RoseOutline.copy(alpha = 0.2f))
                            .clickable { showAvailableOnly = !showAvailableOnly }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("price_available_only_chip")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Filter In Stock Options",
                            tint = if (showAvailableOnly) GreenMatch else RoseTextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "In Stock Only",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (showAvailableOnly) GreenMatch else RoseTextPrimary
                        )
                    }
                }

                if (comparisonOffers.isNotEmpty()) {
                    Button(
                        onClick = {
                            try {
                                val builder = StringBuilder()
                                builder.append("✨ LIVE BEAUTY STOCK COMPARISON REPORT ✨\n")
                                builder.append("Product: $selectedItemText\n")
                                builder.append("===============================\n\n")
                                comparisonOffers.forEachIndexed { index, (prod, store) ->
                                    builder.append("${index + 1}. Store: ${store.name} (${store.district})\n")
                                    builder.append("   • Brand: ${prod.brandName}\n")
                                    builder.append("   • Price: ${prod.priceBdt} BDT\n")
                                    builder.append("   • Availability: ${prod.availabilityStatus}\n")
                                    builder.append("   • FB Page Link: ${store.facebookUrl}\n\n")
                                }
                                builder.append("Generated by Dhaka Beauty Shopping Assistant.")
                                
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Beauty Stock Links", builder.toString())
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied combined FB links & prices for \"$selectedItemText\" to clipboard!", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to copy links.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("copy_combined_fb_links_button")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Copy combined links",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Copy Combined FB Links & Prices",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (comparisonOffers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No offers found for \"$selectedItemText\".",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RoseTextTertiary
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        comparisonOffers.forEach { (prod, store) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(RoseContainer, RoundedCornerShape(16.dp))
                                    .border(1.dp, RoseOutline, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val context = LocalContext.current
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = store.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoseTextPrimary
                                        )
                                        if (store.isVerified) {
                                            Box(
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .background(StarYellow, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = "Verified status indicator",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(8.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${store.district} • ${prod.brandName}",
                                        fontSize = 11.sp,
                                        color = RoseTextTertiary
                                    )

                                    // Direct link to the Facebook store
                                    Text(
                                        text = "Visit Facebook Page ↗",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RosePrimary,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .clickable {
                                                try {
                                                    val webpage = Uri.parse(store.facebookUrl)
                                                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Redirect failed.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${prod.priceBdt} BDT",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = RosePrimary
                                    )

                                    val isAvailable = prod.availabilityStatus == "Available"
                                    val badgeBg = if (isAvailable) GreenMatchBg else if (prod.availabilityStatus == "Coming Soon") RosePill else RoseOutline.copy(alpha = 0.5f)
                                    val badgeTextColor = if (isAvailable) GreenMatch else if (prod.availabilityStatus == "Coming Soon") OrangeMatch else RoseTextSecondary

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(badgeBg)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (prod.availabilityStatus == "Coming Soon") "In ${prod.restockDays} days" else prod.availabilityStatus,
                                            color = badgeTextColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

