package ds.client

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.common.Status
import kotlin.math.log

class SendingMessages (infinite : Boolean, maxCount : Int, clientId : Int, gson : Gson, logger: Logger){

    var infinite =  infinite;
    var maxCount = maxCount;
    var thread : Thread;
    var clientId = clientId;
    var gson = gson;
    var logger = logger;
    var stopped = false;
    var ip = "";
    var port = 8080;

    private var PAYMENT_URL = "/sequencer/put"

    init {
       thread =  setThread();
    }

    fun run() {
        if (!isRunning()) {
            thread.run();
        }
    }

    fun start() {
        stopped = false;
    }

    fun stop() {
        infinite = false;
        maxCount = 0;
        stopped = true;
    }

    fun isRunning() : Boolean{
        return thread.isAlive;
    }

    private fun setThread() : Thread{
        val t = Thread {
            val generatingPayment = GeneratingPayment(min = 10000, max = 50000);

            while (true) {
                while (stopped) {
                    Thread.sleep(150);
                }
                var clientPayment = generatingPayment.generateNext("default", clientId);
                var success = false;

                var count = 0;
                while (infinite || maxCount > count) {
                    if (success) {
                        clientPayment = generatingPayment.generateNext("default", clientId);
                    }
                    val client = Connection(ip, port,PAYMENT_URL, logger);
                    success = client.sendMessage(ConMethod.PUT, gson.toJson(clientPayment));
                    client.disconnect();
                    if (success) {
                        count++;
                        println(count);
                    }


                    Thread.sleep(50);
                }
                stopped = true;
            }
        }
        return t;
    }
}