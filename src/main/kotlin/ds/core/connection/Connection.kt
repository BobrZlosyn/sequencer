package ds.core.connection

import com.google.gson.Gson
import ds.core.common.Logger
import ds.core.common.Status
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.net.ConnectException
import java.nio.charset.Charset

/**
 * class with defined connection, sending messages
 * @param ip ip where to connect
 * @param port port on which connect
 * @param url adress to connect
 * @param logger instance of Logger
 */
class Connection (ip : String, port: Int, url: String, logger: Logger) {
    private var urlPage : URL;
    private var con : HttpURLConnection;
    var urlPath : String;
    private var logger = logger;

    init {
        urlPath = getURL(ip, port, url);
        urlPage = URL(urlPath);
        con = urlPage.openConnection() as HttpURLConnection;

    }

    /**
     * creates new connection
     */
    fun newConnection(ip : String, port: Int, url: String) {
        con.disconnect();
        urlPath = getURL(ip, port, url);
        urlPage = URL(urlPath);
        con = urlPage.openConnection() as HttpURLConnection;

    }

    /**
     * connect to adress with method
     * @param method HTTP method which to use
     * @return success
     */
    private fun connect (method: ConMethod) : Boolean{

        con.connectTimeout = 5000;
        con.readTimeout = 5000;
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        if (method.equals(ConMethod.PATCH)) {
            con.setRequestProperty("X-HTTP-Method-Override", ConMethod.PATCH.name);
            con.requestMethod = ConMethod.POST.name;
        } else {
            con.requestMethod = method.name;
        }

        try {
            con.connect();
        }catch (e : ConnectException) {
            logger.logWarning ("Couldnt connect to $urlPath")
            return false;
        }

        return true;
    }

    /**
     * sending data in json format
     * @param json string in json format
     */
    private fun sendData(json: String) {
        con.outputStream.use { os ->
            val input = json.toByteArray(Charset.forName("utf-8"));
            os.write(input, 0, input.size)
        }
    }

    /**
     * reads response from connection
     * @return response
     */
    fun readResponse(): String {
        val status = con.responseCode

        if (status != 200) {
            logger.logWarning("Error in reading response from $urlPath with status $status");
            return ""
        }

        val data = BufferedReader(
            InputStreamReader(con.inputStream)
        )

        var inputLine = data.readLine();
        val content = StringBuffer();
        while (inputLine != null) {
            content.append(inputLine);
            inputLine = data.readLine();
        }
        data.close();

        return content.toString();
    }

    /**
     * disconnect connection
     */
    fun disconnect(){
        con.disconnect();
    }

    /**
     * put together url
     * @return url in complete format
     */
    private fun getURL (ip : String, port: Int, url: String ) : String {
        return "http://$ip:$port$url";
    }

    /**
     * sending message with method
     * @param method method which will be used
     * @param json string in json format
     * @return success of sending
     */
    fun sendMessage(method : ConMethod, json: String) : Boolean {
        try {
            var success = connect(method);
            if (success) {
                sendData(json);
                var response = readResponse();

                var status = Gson().fromJson(response, Status::class.java);
                if (status.status.equals("fail")) {
                    return false;
                }
                return true;
            }
        }catch (e: ConnectException) {
            logger.logWarning("Couldnt connect to bank at $urlPath")
        }
        return false;
    }

}