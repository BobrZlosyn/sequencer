package ds.sequencer

import ds.core.Payment
import ds.core.PaymentType
import ds.core.ClientPayment
import java.util.concurrent.ThreadLocalRandom

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