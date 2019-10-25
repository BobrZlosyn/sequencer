package ds.sequencer

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.payment.Payment
import ds.core.common.Status
import java.net.ConnectException

class SendingMessages (gson : Gson, logger: Logger){

    var thread : Thread;
    var gson = gson;
    var logger = logger;
    var ip = "";
    var port = 8080;
    private var array : ArrayList <Payment>;
    private var SHUFFLE_URL = "/shuffler/put"

    init {
        thread =  setThread();
        array = ArrayList();
    }

    fun run() {
        if (!isRunning()) {
            thread.start();
        }
    }

    fun stop() {
        array.clear();
    }

    fun isRunning() : Boolean{
        return thread.isAlive;
    }

    private fun setThread() : Thread{

        val task = {
            while (true) {
                if (array.isEmpty()) {
                    Thread.sleep(250);
                    continue;
                }

                var success = false;
                var payment = array[0];
                try {
                    var client = Connection(ip, port, SHUFFLE_URL, logger)
                    success = client.sendMessage(ConMethod.PUT, gson.toJson(payment));
                }catch (e: ConnectException) {
                    logger.logWarning("Couldnt connect to shuffler");
                }

                if (!success) {
                    Thread.sleep(250);
                } else {
                    array.remove(payment);
                }
            }
        }
        var t = Thread(task);

        return t;
    }

    fun addPayment(payment: Payment) {
        array.add(payment);
    }
}