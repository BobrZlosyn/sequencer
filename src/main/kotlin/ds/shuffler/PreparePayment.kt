package ds.shuffler

import ds.core.Bank
import ds.core.common.Logger

class PreparePayment (sendPayment: SendPayment, logger: Logger, shuffling : Shuffling){
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
        for (i in 0 until banks.size) {
            if (banks[i].ip == bank.ip
                && banks[i].port == bank.port
            ) {
                return false;
            }
        }

        banks.add(bank)
        return true;
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