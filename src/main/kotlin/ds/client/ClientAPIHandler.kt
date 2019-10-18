package ds.client

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.common.Status
import spark.Spark.*
import java.lang.Exception
import java.text.ParseException

class ClientAPIHandler (messaging : SendingMessages, gson : Gson){
    private var messaging = messaging;
    private var gson = gson;
    fun initAPI() {
        getSendingCount()
        getStartThread()
        getStopThread()
    }

    private fun getStopThread() {
        get ("/client/stop","application/json", {
                request, response ->
            try {
                messaging.stop();

                Status("ok", "Sending is stopped.");
            }catch (e: Exception) {
                Logger().logWarning("thread stopping failed")
                Status("failed", "Sending is still running.");
            }

        }, gson::toJson );
    }

    private fun getStartThread() {
        get ("/client/run","application/json", {
                request, response ->
            try {
                messaging.infinite = true;
                messaging.start();

                Status("ok", "Sending is running.");
            }catch (e: Exception) {
                Logger().logWarning("thread starting failed")
                Status("failed", "Sending is still stopped");
            }
        }, gson::toJson );

    }
    private fun getSendingCount() {
        get ("/client/run/:maxCount","application/json", {
                request, response ->

            var error = false;
            try {
                messaging.maxCount = Integer.parseInt(request.params("maxCount"));
            }catch (e: ParseException) {
                Logger().logWarning("parsing parameter failed")
                error = true;
            }

            if (!error) {
                try {
                    messaging.infinite = false;
                    messaging.start();

                    Status("ok", "Temporally sending is running.");
                }catch (e: Exception) {
                    Logger().logWarning("thread starting failed")
                    Status("failed", "Sending is stopped");
                }
            } else {
                Status("failed", "Sending is stopped, wrong parameter input");
            }
        }, gson::toJson );
    }
}