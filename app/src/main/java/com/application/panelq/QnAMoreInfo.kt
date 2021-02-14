
package com.application.panelq
class QnAMoreInfo {
    var panelId:String?=""
    var userId:String?=""
    var date:String?=""
    var howManySolved:Int? = 0
    var question: String ? = ""
    var answer: String ? =""

    constructor() {}
    constructor(
        panelId: String?,
        userId: String?,
        date: String?,
        howManySolved: Int?,
        question: String?,
        answer: String?
    ) {
        this.panelId = panelId
        this.userId = userId
        this.date = date
        this.howManySolved = howManySolved
        this.question = question
        this.answer = answer
    }

}
