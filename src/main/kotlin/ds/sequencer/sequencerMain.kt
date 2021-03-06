import com.google.gson.Gson
import ds.core.common.Addresses
import ds.core.common.Configiruation
import ds.core.common.Status
import ds.core.payment.ClientPayment
import spark.Spark.*
import ds.sequencer.GeneratingWholePayment
import ds.sequencer.SendingMessages

fun main(args: Array<String>) {

    val conf = Configiruation(args, 4);
    var shufflerIP = conf.getValidateIPAtIndex(2)
    var shufflerPort = conf.getValidatePortAtIndex(3);

    val gson = Gson()
    var lastSequence = 0;
    var messaging = SendingMessages(gson, conf.logger)
    messaging.ip = shufflerIP;
    messaging.port = shufflerPort;

    var generatingPayment = GeneratingWholePayment();


    post (Addresses.SEQUENCER_PAYMENT_POST.url, Addresses.JSON_FORMAT.url, {
            request, response ->
        var paymentRaw = request.body();
        conf.logger.logInfo("Receiving $paymentRaw")
        var payment = gson.fromJson(paymentRaw, ClientPayment::class.java);
        var wholePayment = generatingPayment.generateNext(lastSequence, payment);
        messaging.addPayment(wholePayment);
        lastSequence++;
        Status("ok", "Payment will be handled");

    }, gson::toJson);

    messaging.run();


}

