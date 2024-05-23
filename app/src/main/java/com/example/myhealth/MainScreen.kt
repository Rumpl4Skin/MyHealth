package com.example.myhealth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myhealth.Screens.Screen
import com.example.myhealth.models.MainScreenViewModel
import com.example.myhealth.ui.components.DatePickerWithDialog
import com.example.myhealth.ui.components.ManufacturedDate
import com.example.myhealth.ui.components.appbar.ActionsMenu
import com.example.myhealth.ui.components.appbar.AppBarState
import com.example.myhealth.ui.components.appbar.rememberAppBarState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = viewModel()
) {

    val navController = rememberNavController()
    val appBarState = rememberAppBarState(navController)
    LaunchedEffect(key1 = Unit) {//прослушивание нажатий в upBar
        // 2
        Screen.Diary.buttons
            .onEach { button ->
                when (button) {
                    // переход в настройки при срабатывании flow
                    Screen.Diary.AppBarIcons.Calendar -> Screen.Diary.showDialog()

                }
            }
            .launchIn(this)

        Screen.Account.buttons
            .onEach { button ->
                when (button) {
                    // переход в настройки при срабатывании flow
                    Screen.Account.AppBarIcons.Settings -> navController.navigate(Screen.Settings.route)
                }
            }
            .launchIn(this)

        Screen.Stats.buttons
            .onEach { button ->
                when (button) {
                    // переход в настройки при срабатывании flow
                    Screen.Stats.AppBarIcons.Calendar -> Screen.Stats.showDialog()
                }
            }
            .launchIn(this)

        Screen.FoodAdd.buttons
            .onEach { button ->
                when (button) {
                    // переход в настройки при срабатывании flow
                    Screen.FoodAdd.AppBarIcons.Done -> Screen.Diary.showDialog()
                    Screen.FoodAdd.AppBarIcons.Clear -> navController.popBackStack()
                }
            }
            .launchIn(this)

        Screen.SleepAdd.buttons
            .onEach { button ->
                when (button) {
                    // переход в настройки при срабатывании flow
                    Screen.SleepAdd.AppBarIcons.Done -> Screen.Diary.showDialog()
                    Screen.SleepAdd.AppBarIcons.Clear -> navController.popBackStack()
                }
            }
            .launchIn(this)
    }
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        topBar = {
            PlaygroundTopAppBar(
                appBarState = appBarState,
                modifier = Modifier.fillMaxWidth(),
            )
        }


    ) {
        BottomNavGraph(navController = navController, modifier = Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundTopAppBar(
    appBarState: AppBarState,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        navigationIcon = {
            // 3
            val icon = appBarState.navigationIcon
            val callback = appBarState.onNavigationIconClick
            // 4
            if (icon != null) {
                IconButton(onClick = { callback?.invoke() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = appBarState.navigationIconContentDescription
                    )
                }
            }
        },
        title = {
            //5
            val title = appBarState.title
            if (title != null) {
                Text(
                    text = stringResource(title)
                )
            }
        },
        actions = {
            // 6
            val items = appBarState.actions
            if (items.isNotEmpty()) {
                ActionsMenu(
                    items = items,
                    isOpen = menuExpanded,
                    onToggleOverflow = { menuExpanded = !menuExpanded },
                    maxVisibleItems = 3,
                )
            }
        },
        modifier = modifier
    )
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Diary,
        Screen.Stats,
        Screen.Account,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = stringResource(screen.title))
        },
        icon = {
            Icon(
                imageVector = screen.buttomIcon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}