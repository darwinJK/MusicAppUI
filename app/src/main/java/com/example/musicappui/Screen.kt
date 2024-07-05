package com.example.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(val title : String,val route:String){


    sealed class BottomScreen(
        val bTitle: String, val bRoute : String,@DrawableRes val bIcon :Int
    ) :Screen(bTitle,bRoute){
        object Home: BottomScreen(
            "Home",
            "home",
            R.drawable.baseline_music_video_24)

        object Library: BottomScreen(
            "Library",
            "library",
            R.drawable.baseline_library_music_24)

        object Browse: BottomScreen(
            "Browse",
            "browse",
            R.drawable.baseline_apps_24)
    }

    sealed class DrawerScreen(val dTitle : String,val dRoute:String,@DrawableRes val icon :Int)
        : Screen(dTitle,dRoute){

            object Account :DrawerScreen(
                "Account",
                "account",
                //how to create
                //file-tools-resource_manager-add-vector_asset-click_art-search_icon-next-finish
                R.drawable.ic_account //// icons that we setup us drawable item.
            )
            object subscription : DrawerScreen(
                "Subcription",
                "subscribe",
                R.drawable.ic_subscribe
            )
        object AddAccount : DrawerScreen(
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
        }
}
val ScreenInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Browse,
    Screen.BottomScreen.Library
)

val screenInDrawer = listOf(
    Screen.DrawerScreen.Account ,
    Screen.DrawerScreen.subscription,
    Screen.DrawerScreen.AddAccount
)