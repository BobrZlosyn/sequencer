package ds.shuffler

import ds.core.common.Configiruation

    fun main(args: Array<String>) {
        var conf = Configiruation(args, 2);
        var sendPayment = SendPayment(conf.logger);
        var shuffling = Suffling();
        var preparePayment = PreparePayment(sendPayment, conf.logger, shuffling);
        var apiShuffler = APIShuffler(conf.logger, preparePayment, shuffling);
        apiShuffler.APIinit()

        preparePayment.start();
    }

