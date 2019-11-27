package ds.core.common

import ds.core.connection.ConMethod

/**
 * enum for api calls
 */
enum class Addresses (url: String, method : ConMethod) {

    DEFAULT_GET ("/", ConMethod.GET),
    BANK_POST ("/bank", ConMethod.POST) ,
    BANK_ACCOUNT_GET ("/bank/account", ConMethod.GET) ,

    CLIENT_STOP_GET ("/client/stop", ConMethod.GET),
    CLIENT_RUN_GET ("/client/run", ConMethod.GET),
    CLIENT_RUN_COUNT_GET ("/client/run/:maxCount", ConMethod.GET) {
        init {
            parameter = "maxCount";
        }
    },

    SHUFFLER_PAYMENT_POST("/shuffler/payment", ConMethod.POST),
    SHUFFLER_BANK_PUT("/shuffler/bank", ConMethod.PUT),

    SEQUENCER_PAYMENT_POST("/sequencer/payment", ConMethod.POST),

    JSON_FORMAT("application/json", ConMethod.GET)

    ;


    val method = method;
    val url = url;
    var parameter = "";

}