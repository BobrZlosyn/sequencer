package ds.sequencer

import ds.core.payment.Payment
import ds.core.payment.ClientPayment

class GeneratingWholePayment (){

    fun generateNext (seq: Int, payment: ClientPayment) : Payment {
        return Payment(
            type = payment.type,
            money = payment.money,
            name = payment.name,
            id = payment.id,
            sequence = seq
        );
    }


}