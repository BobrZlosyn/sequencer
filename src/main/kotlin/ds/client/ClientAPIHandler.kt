package ds.client

import com.google.gson.Gson
import ds.core.common.Adresses
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
        get (Adresses.CLIENT_STOP_GET.url,Adresses.JSON_FORMAT.url, {
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
        get (Adresses.CLIENT_RUN_GET.url,Adresses.JSON_FORMAT.url, {
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
        get (Adresses.CLIENT_RUN_COUNT_GET.url,Adresses.JSON_FORMAT.url, {
                request, response ->

            var error = false;
            try {
                messaging.maxCount = Integer.parseInt(request.params(Adresses.CLIENT_RUN_COUNT_GET.parameter));
            }catch (e: ParseException) {
                Logger().logWarning("parsing parameter failed")
                error = true;
            }

            var status = "failed";
            var msg = "Sending is stopped, wrong parameter input";
            if (!error) {
                try {
                    messaging.infinite = false;
                    messaging.start();

                    msg = "Temporally sending is running.";
                    status = "ok";
                }catch (e: Exception) {
                    Logger().logWarning("thread starting failed")
                    msg ="Sending is stopped";
                }
            }

            Status(status, msg);
        }, gson::toJson );
    }
}