package ds.core.common

import spark.Spark
import java.lang.Exception
import java.net.BindException
import kotlin.system.exitProcess
import spark.Spark.initExceptionHandler



class Configiruation (args : Array<String>, expectedSize : Int) {

    private var args = args;
    private var expectedSize = expectedSize;

    private var ipIndex = 0;
    private var portIndex = 1;
    var logger = Logger();

    init {
        var valid = validate();
        if (!valid) {
            logger.logSevere("Wrong arguments. It should be ip, port")
            exitProcess(-2);
        }

        Spark.port(getPort());
        Spark.ipAddress(getIP())
        initExceptionHandler { e ->
            logger.logSevere("Address already in use");
            exitProcess(-6)
        }
    }

    fun validate () : Boolean {
        if (args.size < expectedSize) {
            return false
        }

        if (!validateIP(args.get(ipIndex))) {
            return false;
        }

        if (!validatePort(args.get(portIndex))) {
            return false
        }

        return true;
    }

    fun getIP() : String {
        return args[ipIndex];
    }

    fun getPort() : Int {
        return getValidatedPort(portIndex);
    }

    private fun getValidatedPort(index: Int) : Int {
        var port = 0
        try {
            port = Integer.parseInt(args[index]);
        } catch (e : Exception) {
            logger.logSevere("Port has bad format");
            exitProcess(-5)
        }

        return port;
    }

    fun getValidatePortAtIndex(index : Int) : Int{
        validateIndex(index);
        if (validatePort(args[index])) {
            return getValidatedPort(index);
        }
        logger.logSevere("Port has bad format");
        exitProcess(-5);
    }

    fun getValidateIPAtIndex(index : Int) : String{
        validateIndex(index);
        if (validateIP(args[index])) {
            return args[index]
        }

        logger.logSevere("IP has bad format");
        exitProcess(-5);
    }

    private fun validateIndex(index: Int) {
        if (index >= args.size) {
            logger.logSevere("Not enough arguments for this index");
            exitProcess(-3)
        }
    }

    private fun validateIP(ip: String): Boolean {
        val pattern =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$"
        return ip.matches(pattern.toRegex())
    }

    private fun validatePort(port : String) : Boolean{
        var pom = Int.MAX_VALUE;
        if (port.length > 5) {
            return false;
        }

        try {
            pom = Integer.parseInt(port);
        }catch (e : Exception) {
            return false;
        }

        return (pom in 1..65534)
    }
}