package ds.sequencer

import com.google.gson.Gson
import ds.core.connection.ConMethod
import ds.core.connection.Connection
import ds.core.payment.Payment
import ds.core.common.Status
import ds.shuffler.sendPaymentsToBank
import java.net.ConnectException

class SendingMessages (gson : Gson){

    var thread : Thread;
    var gson = gson;

    private var array : ArrayList <Payment>;
    private var SHUFFLE_URL = "http://localhost:8082/shuffler/put"

    init {
        thread =  setThread();
        array = ArrayList();
    }

    fun run() {
        if (!isRunning()) {
            thread.run();
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
                var payment = array.get(0);
                try {
                    success = sendPaymentToShuffle(payment);
                }catch (e: ConnectException) {
                    println("Couldnt connect to shuffler");
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


    private fun sendPaymentToShuffle(wholePayment: Payment) : Boolean {
        val client = Connection(SHUFFLE_URL);
        val gson = Gson();
        var success  = client.connect(ConMethod.PUT);
        if (success) {
            client.sendData(gson.toJson(wholePayment));
            var response = client.readResponse();
            var status = gson.fromJson(response, Status::class.java);
            if (!status.status.equals("ok")) {
                println(response);
            }

            return true;
        }
        return false

    }

    fun addPayment(payment: Payment) {
        array.add(payment);
    }
}