import ds.bank.Account
import com.google.gson.Gson
import ds.bank.APIHandler
import ds.bank.ManagePayments
import ds.bank.SendInfo
import ds.core.Bank
import ds.core.common.Configiruation


fun main(args: Array<String>) {
    val conf = Configiruation(args, 4);
    var shuffleIp = conf.getValidateIPAtIndex(2);
    var shufflePort = conf.getValidatePortAtIndex(3);
    var bank = Bank("/bank/patch", conf.getIP(), conf.getPort())


    val defaultAccount = Account(name = "default", money = 5000000, id = 1);
    var accounts = HashMap<String, Account>();

    val gson = Gson()
    accounts.put(defaultAccount.name, defaultAccount);
    var managePayments = ManagePayments(account = accounts, logger = conf.logger);
    var sendBankInfo = SendInfo(shuffleIp, shufflePort, conf.logger, gson.toJson(bank))

    var api = APIHandler(gson, accounts, managePayments, conf.logger);
    api.initApi();
    sendBankInfo.start();

}