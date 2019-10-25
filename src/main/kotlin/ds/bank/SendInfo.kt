package ds.bank

import ds.core.common.Logger
import ds.core.connection.ConMethod
import ds.core.connection.Connection

class SendInfo (shuffleIP : String, shufflePort : Int, logger: Logger, jsonBank: String) {

    private var thread : Thread;
    private var shuffleURL = "/shuffler/post"
    private var shuffleIP = shuffleIP;
    private var shufflePort = shufflePort;
    private var logger = logger;
    private var bank = jsonBank;

    init {
        thread = threadDefine();

    }

    fun start() {
        if (!thread.isAlive) {
            thread.start();
        }
    }

    private fun threadDefine() : Thread {
        return Thread {
            while (true) {
                Thread.sleep(500);

                var client = Connection(shuffleIP, shufflePort, shuffleURL, logger);
                var success = client.sendMessage(ConMethod.POST, bank);
                client.disconnect();
                if (success) {
                    break;
                }else {
                    logger.logInfo("Couldnt sent bank info to shuffler. Sending again");
                }

            }
        }
    }
}