package ds.core

import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.nio.charset.Charset


class Connection (url: String) {
    var urlPage : URL;
    var con : HttpURLConnection;

    init {
        urlPage = URL(url);
        con = urlPage.openConnection() as HttpURLConnection;

    }

    fun newConnection(url: String) {
        con.disconnect();
        urlPage = URL(url);
        con = urlPage.openConnection() as HttpURLConnection;

    }

    fun connect (method: ConMethod) {

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

        con.connect();
    }

    fun sendData(json: String) {
        con.outputStream.use { os ->
            val input = json.toByteArray(Charset.forName("utf-8"));
            os.write(input, 0, input.size)
        }
    }

    fun readResponse(): String {
        val status = con.responseCode

        println(con.responseCode);
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

    fun disconnect(){
        con.disconnect();
    }

}