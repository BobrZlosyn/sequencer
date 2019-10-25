package ds.shuffler

import ds.core.Bank
import ds.core.common.Logger
import java.util.function.Consumer

class PreparePayment (sendPayment: SendPayment, logger: Logger, shuffling : Suffling){
    var thread : Thread
    var banks = ArrayList<Bank>();
    var sendPayment = sendPayment;
    var logger = logger;
    var shuffling = shuffling;

    init {
        thread = getShuffle();
    }

    fun start() {
        thread.start();
    }

    fun addBank(bank : Bank) :Boolean {
        var exists = false;
        for (i in 0 until banks.size) {
            if (banks[i].ip == bank.ip
                && banks[i].port == bank.port
            ) {
                exists = true;
            }
        }

        if (!exists) {
            banks.add(bank)
            exists = true;
        }
        return exists;
    }

    private fun getShuffle () : Thread {
        return Thread {
            while (true) {
                Thread.sleep(1000);
                var payments = shuffling.getCopy();
                if (payments.isEmpty()) {
                    Thread.sleep(200);
                    continue;
                }

                banks.forEach { bank ->
                    payments.shuffle();
                    sendPayment.addPayment(bank, payments);
                }
            }
        };
    }

}