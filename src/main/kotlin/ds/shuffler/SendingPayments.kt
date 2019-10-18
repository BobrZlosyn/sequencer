package ds.shuffler

import com.google.gson.Gson
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.payment.Payment
import java.net.ConnectException

class SendingPayments {

    var thread : Thread
    init {
        thread = shuffle()
        thread.run();
    }

    private fun shuffle () : Thread {
        val t = Thread {
            while (true) {
                Thread.sleep(400);
                var payments = suffling.getCopy();
                banks.forEach { bank ->
                    payments.shuffle();
                    var success = true;
                    if (bank.restPayments.isNotEmpty()) {
                        success = sendPaymentsToBank(bank.restPayments, bank.url);
                        if (!success) {
                            banks.get(banks.indexOf(bank)).restPayments.addAll(payments);
                        }else {
                            banks.get(banks.indexOf(bank)).restPayments.clear();
                        }
                    }

                    if (success && payments.isNotEmpty()) {
                        success = sendPaymentsToBank(payments, bank.url);
                        if (!success) {
                            banks.get(banks.indexOf(bank)).restPayments.addAll(payments);
                        }
                    }
                }
            }
        }
        return t;
    }

    private fun sendPaymentsToBank(payments: ArrayList<Payment>, url : String) :Boolean{
        try {
            val client = Connection(url);
            val gson = Gson();
            var success = client.connect(ConMethod.PATCH);
            if (success) {
                client.sendData(gson.toJson(payments.toArray()));
                var response = client.readResponse();
                return true;
            }

        }catch (e: ConnectException) {
            println("Couldnt connect to bank at $url")
        }
        return false;
    }
}