package ds.core.common

import java.lang.Exception

class Configiruation (args : Array<String>, expectedSize : Int) {

    private var args = args;
    private var expectedSize = expectedSize;

    private var IPindex = 0;
    private var portIndex = 1;
    fun validate () : Boolean {
        if (args.size < expectedSize) {
            return false
        }

        if (!validateIP(args.get(IPindex))) {
            return false;
        }

        if (!validatePort(args.get(portIndex))) {
            return false
        }

        return true;
    }

    fun getIP() : String {
        return args.get(IPindex);
    }

    fun getPort() : Int {
        return Integer.parseInt(args.get(portIndex));
    }

    private fun validateIP(ip: String): Boolean {
        val PATTERN =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$"
        return ip.matches(PATTERN.toRegex())
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