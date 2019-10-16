import com.google.gson.Gson
import ds.core.Payment
import ds.core.ClientPayment
import ds.core.ConMethod
import spark.Spark.*
import ds.core.Connection
import ds.sequencer.GeneratingWholePayment

private var PAYMENT_URL = "/sequencer/put"
private var SHUFFLE_URL = "http://localhost:8082/shuffler/put"


fun main(args: Array<String>) {
    port(8081);
    val gson = Gson()
    var lastSequence = 0;

    var generatingPayment = GeneratingWholePayment();
    put (PAYMENT_URL, "application/json", {
            request, response ->
        var paymentRaw = request.body();
        var payment = gson.fromJson(paymentRaw, ClientPayment::class.java);
        var wholePayment = generatingPayment.generateNext(lastSequence, payment);
        sendPaymentToShuffle(wholePayment);
        lastSequence++;

        "ok"
    }, gson::toJson);

}

fun sendPaymentToShuffle(wholePayment: Payment) {
    val client = Connection(SHUFFLE_URL);
    val gson = Gson();
    client.connect(ConMethod.PUT);
    client.sendData(gson.toJson(wholePayment));
    var response = client.readResponse();
    println(response);
}