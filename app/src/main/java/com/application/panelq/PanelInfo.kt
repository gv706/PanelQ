package com.application.panelq

class PanelInfo {
    var id:String?=""
    var name: String ? = ""
    var description: String ? =""
    var profile: String ? = ""
    var createdOn:String?= ""
    constructor() {}
    constructor(panelId:String?,panelName:String?, panelDescription:String?, panelProfile: String?,panelCreatedOn:String?) {
        this.id=panelId
        this.name = panelName
        this.description = panelDescription
        this.profile = panelProfile
        this.createdOn=panelCreatedOn
    }
}
