package ds.core

import ds.core.connection.ConMethod
import ds.core.payment.Payment

class Bank (url : String, ip : String, port : Int, method : ConMethod) {
    var url = url
    var ip = ip
    var port = port
    var method = method

    var restPayments : ArrayList <Payment> = ArrayList();


}