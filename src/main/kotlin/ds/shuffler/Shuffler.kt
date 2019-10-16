package ds.shuffler

import com.google.gson.Gson
import ds.core.ClientPayment
import ds.core.Payment
import ds.sequencer.GeneratingWholePayment
import spark.Spark.*
import java.util.concurrent.Semaphore

private var BANK_URL = "http://localhost:8084/bank/patch"
private var SHUFFLE_URL = "/shuffler/put"


    var suffling = Suffling();

    fun main(args: Array<String>) {
        port(8082);
        val gson = Gson();


        var generatingPayment = GeneratingWholePayment();
        put (SHUFFLE_URL, "application/json", {
                request, response ->
            var paymentRaw = request.body();
            var payment = gson.fromJson(paymentRaw, Payment::class.java);
            suffling.addToList(payment);
            println(paymentRaw);

            "ok"
        }, gson::toJson);

    }

    fun suffle () {
        val t = Thread {

            while (true) {
                Thread.sleep(500);
                suffling.suffleList();
            }
        }
    }