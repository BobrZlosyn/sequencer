package ds.client

import com.google.gson.Gson
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.common.Status
import ds.shuffler.sendPaymentsToBank

class SendingMessages (infinite : Boolean, maxCount : Int, clientId : Int, gson : Gson){

    var infinite =  infinite;
    var maxCount = maxCount;
    var thread : Thread;
    var clientId = clientId;
    var gson = gson;
    var stopped = false;

    private var PAYMENT_URL = "http://localhost:8081/sequencer/put"

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
                    val client = Connection(PAYMENT_URL);
                    success = client.connect(ConMethod.PUT);
                    if (success) {
                        client.sendData(gson.toJson(clientPayment));
                        var response = client.readResponse();
                        client.disconnect();
                        count++;
                        println("dddddddd")
                        var status = gson.fromJson(response, Status::class.java);
                        if (status.status.equals("fail")) {
                            println(response);
                        }
                    }
                    Thread.sleep(50);
                }
                stopped = true;
            }
        }
        return t;
    }
}