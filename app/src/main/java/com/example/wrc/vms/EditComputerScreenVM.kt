package com.example.wrc.vms

import com.example.wrc.data.ComputerData
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService

class EditComputerScreenVM : FormViewModel(), IService {

    lateinit var dataService: DataLocalService
    lateinit var loadData: ComputerData
    var hostName = state("") { validateRule(String::isNotEmpty,"Не должно быть пустым") }
    var ip = state("") { validateRule(String::isNotEmpty,"Не должно быть пустым") }
    var port = state("") {
        validateRule(String::isNotEmpty,"Не должно быть пустым")
        validateFilter("Только цифры") { it.all(Char::isDigit)}
    }


    override fun inject(provider: DIContext) {
        dataService = provider.get(DataLocalService::class)
    }
    fun load(uuid: String){
        loadData = dataService.get<ComputerData>().first() {it.id == uuid}
        hostName.changeValue(loadData.name)
        ip.changeValue(loadData.ip)
        port.changeValue(loadData.port.toString())
    }

    fun save(): Boolean{
        if(!validateAll()){
            return false;
        }
        dataService.updateAndSave(loadData) {it.name = hostName.value; it.ip = ip.value; it.port = port.value.toInt(); it}
        return true;
    }
}