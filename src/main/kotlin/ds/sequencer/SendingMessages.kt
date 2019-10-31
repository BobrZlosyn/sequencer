package ds.sequencer

import com.google.gson.Gson
import ds.core.common.Adresses
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
                    Thread.sleep(200);
                    continue;
                }

                var success = false;
                var payment = array[0];
                try {
                    var client = Connection(ip, port, Adresses.SHUFFLER_PAYMENT_POST.url, logger)
                    success = client.sendMessage(Adresses.SHUFFLER_PAYMENT_POST.method, gson.toJson(payment));
                }catch (e: ConnectException) {
                    logger.logWarning("Could'nt connect to shuffler");
                }

                if (!success) {
                    Thread.sleep(250);
                } else {
                    array.remove(payment);
                }
            }
        }

        return Thread(task);
    }

    fun addPayment(payment: Payment) {
        array.add(payment);
    }
}