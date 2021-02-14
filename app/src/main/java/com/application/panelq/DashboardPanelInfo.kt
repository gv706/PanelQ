package com.application.panelq

class DashboardPanelInfo {

    var userid: String ? =""
    var panelid: String ? = ""
    var solved:String?= ""
    constructor() {}
    constructor(userId:String?,panelId:String?,solved:String?) {

        this.userid = userId
        this.panelid= panelId
        this.solved = solved
    }
}
