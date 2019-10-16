package ds.client

import ds.core.ClientPayment
import ds.core.PaymentType
import java.util.concurrent.ThreadLocalRandom

class GeneratingPayment (min: Int, max: Int){
    var min = min
    var max = max

    fun generateNext (name: String, id : Int) : ClientPayment {
        return ClientPayment(money = generateMoney(),
                            name = name,
                            id = id,
                            type = generateType());

    }

    private fun generateType() : PaymentType {
        var random = ThreadLocalRandom.current().nextInt(0, 2);
        println(random)

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