package ds.shuffler

import com.google.gson.Gson
import ds.core.ConMethod
import ds.core.Connection
import ds.core.Payment
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
            println(paymentRaw);

            "ok"
        }, gson::toJson);

        suffle();
    }

    fun bankInit() {
        banks.add(Bank("http://localhost:8084/bank/patch"));
    }

    fun suffle () {
        val t = Thread {

            while (true) {
                Thread.sleep(400);
                var payments = suffling.getCopy();
                if (payments.isEmpty()) {
                    continue;
                }
                println(payments)
                banks.forEach { bank ->
                    payments.shuffle();
                    sendPaymentsToBank(payments, bank.url);
                }
            }
        }
        t.run();
    }

    fun sendPaymentsToBank(payments: ArrayList<Payment>, url : String) {
        val client = Connection(url);
        val gson = Gson();
        client.connect(ConMethod.PATCH);
        client.sendData(gson.toJson(payments.toArray()));
        var response = client.readResponse();
        println(response);
    }