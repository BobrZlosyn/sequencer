package ds.shuffler

import com.google.gson.Gson
import ds.core.Bank
import ds.core.common.Logger
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.payment.Payment
import java.net.ConnectException
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadLocalRandom

class SendPayment  (logger: Logger){

    var banks = ArrayList<Bank>();
    var thread : Thread
    var logger = logger;
    private var gson = Gson()

    var semaphore = Semaphore(1);
    init {
        thread = selectPayment()
        thread.start();
    }

    private fun selectPayment () : Thread {
        return Thread {
            while (true) {
                Thread.sleep(40);
                if (banks.isEmpty()) {
                    continue;
                }

                var index = selectBankRandom(banks.size);
                var bank = banks[index];
                semaphore.acquire();
                var payments = bank.restPayments;
                if (payments.isNotEmpty()) {
                    var payment = payments[0];
                    var con = Connection(bank.ip, bank.port, bank.url, logger);
                    var success = con.sendMessage(ConMethod.PATCH, Gson().toJson(payment));
                    if (success) {
                        banks[index].restPayments.remove(payment);
                    }
                } else {
                    banks.removeAt(index);
                }
                semaphore.release();
            }
        };
    }

    private fun selectBankRandom(max : Int) : Int{
        return ThreadLocalRandom.current().nextInt(0, max);
    }

    fun addPayment(bank: Bank, payments : ArrayList<Payment>) {

        semaphore.acquire();
        var exist = banks.contains(bank);
        if (!exist) {
            banks.add(bank)
        }
        banks.get(banks.indexOf(bank)).restPayments.addAll(payments);
        banks.get(banks.indexOf(bank)).restPayments.shuffle();
        println(banks.get(banks.indexOf(bank)).restPayments.size)
        semaphore.release();
    }
}