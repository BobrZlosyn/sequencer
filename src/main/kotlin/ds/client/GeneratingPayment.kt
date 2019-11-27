package ds.client

import ds.core.payment.ClientPayment
import ds.core.payment.PaymentType
import java.util.concurrent.ThreadLocalRandom

class GeneratingPayment (min: Int, max: Int){
    var min = min
    var max = max

    fun generateNext (name: String, clientId : Int) : ClientPayment {
        return ClientPayment(
            money = generateMoney(),
            name = name,
            clientId = clientId,
            type = generateType()
        );

    }

    private fun generateType() : PaymentType {
        var random = ThreadLocalRandom.current().nextInt(0, 2);
        return if (random == 0) {
            PaymentType.CREDIT;
        } else {
            PaymentType.DEBIT;
        }
    }

    private fun generateMoney() : Int{
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}