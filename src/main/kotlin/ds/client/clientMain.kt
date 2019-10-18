import com.google.gson.Gson
import ds.client.ClientAPIHandler
import ds.client.SendingMessages
import spark.Spark.*
import java.util.concurrent.ThreadLocalRandom



fun main(args: Array<String>) {

    port(8080);

    var clientId = ThreadLocalRandom.current().nextInt(1, 9999);
    println("Was created client with id: $clientId");


    var infinite = false;
    var maxCount = 111;

    var gson = Gson();
    var messaging = SendingMessages(infinite, maxCount, clientId, gson)
    var clientAPI = ClientAPIHandler(messaging, gson);
    clientAPI.initAPI();
    messaging.run();
}

