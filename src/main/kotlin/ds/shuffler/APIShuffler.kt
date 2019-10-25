package ds.shuffler

import com.google.gson.Gson
import ds.core.Bank
import ds.core.common.Logger
import ds.core.common.Status
import ds.core.payment.Payment
import spark.Spark.post
import spark.Spark.put
import java.lang.Exception


class APIShuffler (logger: Logger, preparePayment: PreparePayment, shuffling : Suffling){
    var logger = logger;
    val gson = Gson();
    var preparePayment = preparePayment;
    var shuffling = shuffling;

    private var SHUFFLE_URL = "/shuffler/put"
    private var SHUFFLE_URL_ADD_BANK = "/shuffler/post"

    fun APIinit() {
        putPayment();
        postBank();
    }

    private fun putPayment() {
        put(SHUFFLE_URL, "application/json", { request, response ->
            var paymentRaw = request.body();
            logger.logInfo("Receive $paymentRaw");
            var payment = gson.fromJson(paymentRaw, Payment::class.java);
            shuffling.addToList(payment);

            Status("ok", "Payments will be shuffled");
        }, gson::toJson);

    }

    private fun postBank() {
        post(SHUFFLE_URL_ADD_BANK, "application/json", { request, response ->
            var bankRaw = request.body();
            //logger.logInfo("Receive $bankRaw");

            try {
                var bank = gson.fromJson(bankRaw, Bank::class.java);
                var result = preparePayment.addBank(bank);
                if (result) {
                    logger.logInfo("Bank ip: " + bank.ip + " port: " + bank.port + " registered")
                    Status("ok", "Bank registered");
                } else {
                    logger.logInfo("Bank ip: " + bank.ip + " port: " + bank.port + " already registered")
                    Status("ok", "Bank already registered");
                }
            } catch (e: Exception) {
                logger.logWarning("Couldnt parse object");
                //println(e.printStackTrace())
                Status("fail", "Couldnt parse object")
            }

        }, gson::toJson);
    }
}