package ds.bank

import ds.core.common.Addresses
import ds.core.common.Logger
import ds.core.connection.Connection

/**
 * Thread with Sending bank connection info to shuffler
 * @param shuffleIP ip of Shuffler
 * @param shufflePort port of Shuffler
 * @param logger instance of logger
 * @param jsonBank bank info in json format
 */
class SendInfo (shuffleIP : String, shufflePort : Int, logger: Logger, jsonBank: String) {

    private var thread : Thread;
    private var shuffleIP = shuffleIP;
    private var shufflePort = shufflePort;
    private var logger = logger;
    private var bank = jsonBank;

    init {
        thread = threadDefine();
    }

    /**
     * starting thread
     */
    fun start() {
        if (!thread.isAlive) {
            thread.start();
        }
    }

    /**
     * defining thread
     */
    private fun threadDefine() : Thread {
        return Thread {
            var interval: Long = 500;
            while (true) {
                Thread.sleep(interval);

                var client = Connection(shuffleIP, shufflePort, Addresses.SHUFFLER_BANK_PUT.url, logger);
                var success = client.sendMessage(Addresses.SHUFFLER_BANK_PUT.method, bank);
                client.disconnect();

                if (success) {
                    interval = 60000;
                }else {
                    logger.logInfo("Couldnt sent bank info to shuffler. Sending again");
                    interval = 500;
                }
            }
        }
    }
}