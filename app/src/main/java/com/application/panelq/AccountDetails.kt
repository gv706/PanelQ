package com.application.panelq

class AccountDetails {
    var name: String ? = ""
    var gender: String ? =""
    var email: String ? = ""
    var phone:String ? = ""
    var countryCode : String ? =""
    constructor() {}
    constructor(name:String?,gender:String?,countryCode:String?,phone:String?,email:String?) {
        this.name = name
        this.gender = gender
        this.email = email
        this.phone = phone
        this.countryCode = countryCode
    }
}