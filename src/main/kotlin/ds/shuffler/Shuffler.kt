package ds.shuffler

import com.google.gson.Gson
import ds.core.Bank
import ds.core.common.Configiruation
import ds.core.payment.Payment
import ds.core.common.Status
import ds.sequencer.GeneratingWholePayment
import spark.Spark.*
import java.lang.Exception




    fun main(args: Array<String>) {
        var conf = Configiruation(args, 2);
        var sendPayment = SendPayment(conf.logger);
        var shuffling = Suffling();
        var preparePayment = PreparePayment(sendPayment, conf.logger, shuffling);
        var apiShuffler = APIShuffler(conf.logger, preparePayment, shuffling);
        apiShuffler.APIinit()

        preparePayment.start();
    }

