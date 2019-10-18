package ds.shuffler

import com.google.gson.Gson
import ds.core.payment.Payment
import ds.core.common.Status
import ds.sequencer.GeneratingWholePayment
import spark.Spark.*

private var BANK_URL = "http://localhost:8084/bank/patch"
private var SHUFFLE_URL = "/shuffler/put"


    var suffling = Suffling();
    var banks = ArrayList<Bank>();

    fun main(args: Array<String>) {
        port(8082);
        val gson = Gson();
        bankInit();

        var generatingPayment = GeneratingWholePayment();
        put (SHUFFLE_URL, "application/json", {
                request, response ->
            var paymentRaw = request.body();
            var payment = gson.fromJson(paymentRaw, Payment::class.java);
            suffling.addToList(payment);

            Status("ok", "Payments will be shuffled");
        }, gson::toJson);

        var shuffle = SendingPayments();
    }

    fun bankInit() {
        banks.add(Bank("http://localhost:8084/bank/patch"));
    }

