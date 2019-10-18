import ds.bank.Account
import spark.Spark.*
import com.google.gson.Gson
import ds.bank.APIhandler
import ds.bank.ManagePayments
import ds.core.common.Logger


fun main(args: Array<String>) {

    var logger = Logger();

    /*val conf = Configiruation(args, 2);
    var valid = conf.validate();
    if (!valid) {
        logger.logSevere("Wrong arguments. It should be ip, port")
        exitProcess(-1);
    }

    port(conf.getPort());
    ipAddress(conf.getIP())
    */

    val defaultAccount = Account(name = "default", money = 5000000, id = 1);
    port(8084)

    val gson = Gson()
    var accounts = HashMap<String, Account>();
    accounts.put(defaultAccount.name, defaultAccount);
    var managePayments = ManagePayments(account = accounts);

    var api = APIhandler(gson, accounts, managePayments, logger);

}