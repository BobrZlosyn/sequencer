package ds.bank

import com.google.gson.Gson
import ds.core.Bank
import ds.core.common.Adresses
import ds.core.common.Logger
import ds.core.payment.Payment
import ds.core.common.Status
import spark.Spark

/**
 *  Defines api for bank, and serves it
 *  @param gson Gson instance
 *  @param accounts array of all accounts
 *  @param managePayments instance of ManagePayemnt class
 *  @param logger logger instance
 *  @param bank this bank info instance
 */
class APIHandler (gson: Gson, accounts : HashMap<String, Account>, managePayments: ManagePayments, logger: Logger, bank: Bank) {

    var gson = gson;
    var accounts = accounts;
    var managePayments = managePayments;
    var paymentReceived = 0;
    var logger = logger;
    val bank = bank;

    fun initApi() {
        getWelcome()
        getRemainMoney()
        patchMoney();
    }

    /**
     * default welcome address with info about bank
     * @return bank in json format
     */
    private fun getWelcome() {
        Spark.get(Adresses.DEFAULT_GET.url, Adresses.JSON_FORMAT.url, { _, _ ->
            bank
        }, gson::toJson);
    }

    /**
     * shows status of accounts
     * @return accounts in json format
     */
    private fun getRemainMoney() {
        Spark.get(Adresses.BANK_ACCOUNT_GET.url, Adresses.JSON_FORMAT.url, { _, _ ->
            accounts
        }, gson::toJson);
    }

    /**
     * add payment to array to process
     * @return Status class with result
     */
    private fun patchMoney () {

        Spark.patch( Adresses.BANK_PATCH.url, Adresses.JSON_FORMAT.url, { request, _ ->
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