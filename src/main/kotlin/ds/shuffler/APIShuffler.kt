package ds.shuffler

import com.google.gson.Gson
import ds.core.Bank
import ds.core.common.Addresses
import ds.core.common.Logger
import ds.core.common.Status
import ds.core.payment.Payment
import spark.Spark.post
import spark.Spark.put
import java.lang.Exception


class APIShuffler (logger: Logger, preparePayment: PreparePayment, shuffling : Shuffling){
    var logger = logger;
    val gson = Gson();
    var preparePayment = preparePayment;
    var shuffling = shuffling;

    fun APIinit() {
        putPayment();
        postBank();
    }

    private fun putPayment() {
        post (Addresses.SHUFFLER_PAYMENT_POST.url, Addresses.JSON_FORMAT.url, { request, response ->
            var paymentRaw = request.body();
            logger.logInfo("Receive $paymentRaw");
            var payment = gson.fromJson(paymentRaw, Payment::class.java);
            shuffling.addToList(payment);

            Status("ok", "Payments will be shuffled");
        }, gson::toJson);

    }

    private fun postBank() {
        put (Addresses.SHUFFLER_BANK_PUT.url, Addresses.JSON_FORMAT.url, { request, response ->
            var bankRaw = request.body();
            //logger.logInfo("Receive $bankRaw");

            var msg = "Could'nt parse object";
            var status = "fail";

            try {
                var bank = gson.fromJson(bankRaw, Bank::class.java);
                var result = preparePayment.addBank(bank);


                status = "ok"
                msg = if (result) {
                    logger.logInfo("Bank ip: " + bank.ip + " port: " + bank.port + " registered")
                    "Bank registered";
                } else {
                    logger.logInfo("Bank ip: " + bank.ip + " port: " + bank.port + " already registered")
                    "Bank already registered";
                }

            } catch (e: Exception) {
                logger.logWarning("Couldnt parse object");
                //println(e.printStackTrace())
            }

            Status(status, msg)
        }, gson::toJson);
    }
}