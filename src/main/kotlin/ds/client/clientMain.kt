import com.google.gson.Gson
import ds.client.GeneratingPayment
import ds.core.ConMethod
import spark.Spark.*
import ds.core.Connection
import java.util.concurrent.ThreadLocalRandom

private var PAYMENT_URL = "http://localhost:8081/sequencer/put"


fun main(args: Array<String>) {
    port(8080);

    var clientId = ThreadLocalRandom.current().nextInt(1, 9999);
    println("Was created client with id: $clientId");
    var infinite = false;
    var maxCount = 2;
    var count = 0;

     val gson = Gson();
    val generatingPayment = GeneratingPayment(min = 10000, max = 50000);
    while (infinite || maxCount > count) {
        var clientPayment = generatingPayment.generateNext("default", clientId);
        val client = Connection(PAYMENT_URL);
        client.connect(ConMethod.PUT);
        client.sendData(gson.toJson(clientPayment));
        var response = client.readResponse();
        client.disconnect();

        count++;
        println(response);
    }

}