package com.example.musicappui.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.ui.theme.AccountDialog
import com.example.musicappui.MainViewModel
import com.example.musicappui.Screen
import com.example.musicappui.ScreenInBottom
import com.example.musicappui.screenInDrawer
import com.example.musicappui.ui.theme.AccountView
import com.example.musicappui.ui.theme.BrowserView
import com.example.musicappui.ui.theme.HomeView
import com.example.musicappui.ui.theme.LibraryView
import com.example.musicappui.ui.theme.SubscriptionView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.musicappui.R
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.res.colorResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(){

    val scaffoldState : ScaffoldState= rememberScaffoldState()
    val scope : CoroutineScope= rememberCoroutineScope()
    val viewModel:MainViewModel = viewModel()
    //allow us to find out on which "view" we currently are

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    val controller : NavController= rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute=navBackStackEntry?.destination?.route

    val dialogOpen = remember{ mutableStateOf(false) }

    val currentScreen = remember{viewModel.currentScreen.value}

    //change that to currentScreen. title
    val title = remember{ mutableStateOf(currentScreen.title) }

    val modalSheetState= rememberModalBottomSheetState(
        initialValue= ModalBottomSheetValue.Hidden,
        confirmValueChange = {it!=ModalBottomSheetValue.HalfExpanded}
    )
    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp

    val bottomBar : @Composable ()-> Unit = {
        if(currentScreen is Screen.DrawerScreen
            || currentScreen == Screen.BottomScreen.Home){
            BottomNavigation( backgroundColor = colorResource(id = R.color.liteBlue),
                modifier=Modifier.wrapContentSize()) {
                ScreenInBottom.forEach{
                    item->
                    BottomNavigationItem(
                        selected = currentRoute==item.bRoute,
                        onClick = {controller.navigate(item.bRoute)
                                  title.value=item.bTitle
                                  }, icon = {
                            Icon(painter = painterResource(id=item.bIcon) , contentDescription = item.bTitle)
                        },
                        label= { Text(text = item.bTitle)}
                    , selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black)
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius),
        sheetContent ={
        MoreBottomSheet(modifier=modifier)
    } ){
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(backgroundColor = colorResource(id = R.color.liteBlue),
                    title = { Text(title.value) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if(modalSheetState.isVisible)
                                    modalSheetState.hide()
                                else
                                    modalSheetState.show()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null )
                        }
                    },
                    navigationIcon = { IconButton(onClick = {
                        //todo open drawer

                        scope.launch {
                            scaffoldState.drawerState.open()
                            // we can use drawable state by only using CoroutineScope.
                        }
                    }) {
                        Icon(imageVector= Icons.Default.AccountCircle,contentDescription = "menu")
                    }}
                )
            },scaffoldState=scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)){
                    items(screenInDrawer){
                            item->
                        DrawerItem(selected = currentRoute == item.dRoute , item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute=="add_account"){
                                dialogOpen.value=true

                            }else{
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            }
        ) {
            navigation(navController = controller, viewModel = viewModel, pd = it)

            AccountDialog(dialogOpen = dialogOpen)
        }

    }



}

@Composable
fun DrawerItem(
    selected:Boolean,
    item:Screen.DrawerScreen,
    OnDrawerItemClicked:()->Unit,
){
    val background = if(selected) Color.LightGray else Color.White
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .background(background)
        .clickable {
            OnDrawerItemClicked()
        }){
        Icon(painter = painterResource(id = item.icon), contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(text = item.dTitle,
            style = MaterialTheme.typography.headlineSmall,)
    }

}

@Composable
fun MoreBottomSheet(modifier: Modifier){
   Box(
       Modifier
           .fillMaxWidth()
           .height(300.dp).background(colorResource(id = R.color.liteBlue)),
           ){
       Column(modifier=Modifier.padding(16.dp),
           verticalArrangement = Arrangement.SpaceBetween) {
           Row(modifier=Modifier.padding(16.dp)) {
               Icon(modifier = Modifier.padding(end=8.dp),
                   painter = painterResource(id = R.drawable.baseline_settings_24) ,
                   contentDescription = "settings")
               Text(text = "Settings", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier=Modifier.padding(16.dp)) {
               Icon(modifier = Modifier.padding(end=8.dp),
                   painter = painterResource(id = R.drawable.baseline_share_24) ,
                   contentDescription = "share")
               Text(text = "Share", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier=Modifier.padding(16.dp)) {
               Icon(modifier = Modifier.padding(end=8.dp),
                   painter = painterResource(id = R.drawable.baseline_help_24) ,
                   contentDescription = "help")
               Text(text = "Help", fontSize = 20.sp, color = Color.White)
           }
       }
   }
}

@Composable
fun navigation(navController: NavController,viewModel: MainViewModel,pd: PaddingValues){


    NavHost(navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,modifier=Modifier.padding(pd)){

        composable(Screen.BottomScreen.Home.bRoute){
             HomeView()
        }
        composable(Screen.BottomScreen.Browse.bRoute){
            BrowserView()
        }
        composable(Screen.BottomScreen.Library.bRoute){
          LibraryView()
        }

        composable(Screen.DrawerScreen.Account.route){
            AccountView()
        }
        composable(Screen.DrawerScreen.subscription.route){
            SubscriptionView()
        }

    }

}