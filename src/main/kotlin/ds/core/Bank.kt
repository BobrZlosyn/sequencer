package ds.core

import ds.core.payment.Payment
import ds.core.payment.PaymentType

class Bank (url : String, ip : String, port : Int) {
    var url = url
    var ip = ip
    var port = port

    var restPayments : ArrayList <Payment>;
    init {
        restPayments = ArrayList();
    }


}