package ds.bank

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.payment.Payment
import ds.core.common.Status
import spark.Spark

class APIhandler (gson: Gson, accounts : HashMap<String, Account>, managePayments: ManagePayments, logger: Logger) {

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

    fun getWelcome() {
        Spark.get("/") { req, res -> "Banka vás vítá" }
    }

    fun getRemainMoney() {
        Spark.get("/money", "application/json", { request, response ->
            accounts
        }, gson::toJson);
    }

    fun patchMoney () {

        Spark.patch("/bank/patch", "application/json", { request, response ->
            var paymentsRaw = request.body();
            var paymentArray = gson.fromJson(paymentsRaw, Array<Payment>::class.java)
            managePayments.manageArray(paymentArray);
            logger.logInfo("" + managePayments.paymentsCount);
            Status("ok", "Payment received");
        }, gson::toJson);
    }
}