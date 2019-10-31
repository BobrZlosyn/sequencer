import com.google.gson.Gson
import ds.client.ClientAPIHandler
import ds.client.SendingMessages
import ds.core.common.Configiruation
import java.util.concurrent.ThreadLocalRandom


fun main(args: Array<String>) {
    val conf = Configiruation(args, 4);

    var paymentIP = conf.getValidateIPAtIndex(2)
    var paymentPort = conf.getValidatePortAtIndex(3)

    var maxCount = 0;
    var infinite = false;
    if (args.size > 4) {
        try {
            maxCount = Integer.parseInt(args[4]);
        } catch (e : Exception) {
            conf.logger.logWarning("Wrong count parameter, running infinite sending")
            infinite = true;
        }
    }else {
        infinite = true;
        maxCount = 0;
    }


    var clientId = ThreadLocalRandom.current().nextInt(1, 9999);
    conf.logger.logInfo("Was created client with id: $clientId");

    var gson = Gson();
    var messaging = SendingMessages(infinite, maxCount, clientId, gson, conf.logger)
    messaging.port = paymentPort;
    messaging.ip = paymentIP;
    var clientAPI = ClientAPIHandler(messaging, gson);
    clientAPI.initAPI();
    messaging.run();
}

