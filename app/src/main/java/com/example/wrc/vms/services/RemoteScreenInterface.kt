package com.example.wrc.vms.services

import com.example.wrc.vms.TypeRemotePanel

interface IRemoteScreen {

    fun Home()
    fun SetPanel(type: TypeRemotePanel)

    fun HideBar();
    fun ViewBar();

}