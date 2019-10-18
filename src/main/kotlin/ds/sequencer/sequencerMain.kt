import com.google.gson.Gson
import ds.core.common.Status
import ds.core.payment.ClientPayment
import spark.Spark.*
import ds.sequencer.GeneratingWholePayment
import ds.sequencer.SendingMessages

private var PAYMENT_URL = "/sequencer/put"
private var SHUFFLE_URL = "http://localhost:8082/shuffler/put"


fun main(args: Array<String>) {
    port(8081);
    val gson = Gson()
    var lastSequence = 0;
    var messaging = SendingMessages(gson)
    var generatingPayment = GeneratingWholePayment();

    put (PAYMENT_URL, "application/json", {
            request, response ->
        var paymentRaw = request.body();
        var payment = gson.fromJson(paymentRaw, ClientPayment::class.java);
        var wholePayment = generatingPayment.generateNext(lastSequence, payment);
        messaging.addPayment(wholePayment);
        lastSequence++;
        Status("ok", "Payment will be handled");

    }, gson::toJson);

    messaging.run();


}

