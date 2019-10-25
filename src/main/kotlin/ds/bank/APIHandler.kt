package ds.bank

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.payment.Payment
import ds.core.common.Status
import spark.Spark

class APIHandler (gson: Gson, accounts : HashMap<String, Account>, managePayments: ManagePayments, logger: Logger) {

    var gson = gson;
    var accounts = accounts;
    var managePayments = managePayments;
    var paymentReceived = 0;
    var logger = logger;

    fun initApi() {
        getWelcome()
        getRemainMoney()
        patchMoney();
    }

    private fun getWelcome() {
        Spark.get("/") { _, _ -> "Banka vás vítá" }
    }

    private fun getRemainMoney() {
        Spark.get("/money", "application/json", { _, _ ->
            accounts
        }, gson::toJson);
    }

    private fun patchMoney () {

        Spark.patch("/bank/patch", "application/json", { request, _ ->
            var paymentRaw = request.body();
            //logger.logInfo("Receiving $paymentRaw");
            var payment = gson.fromJson(paymentRaw, Payment::class.java)
            managePayments.manageArray(payment);
            paymentReceived++;
           //println("received: $paymentReceived")

            Status("ok", "Payment received");
        }, gson::toJson);
    }
}