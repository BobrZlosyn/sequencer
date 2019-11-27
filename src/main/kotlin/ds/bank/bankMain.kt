import ds.bank.Account
import com.google.gson.Gson
import ds.bank.APIHandler
import ds.bank.ManagePayments
import ds.bank.SendInfo
import ds.core.Bank
import ds.core.common.Addresses
import ds.core.common.Configiruation


fun main(args: Array<String>) {
    val conf = Configiruation(args, 4);
    var shuffleIp = conf.getValidateIPAtIndex(2);
    var shufflePort = conf.getValidatePortAtIndex(3);

    // creating info class
    var bank = Bank(Addresses.BANK_POST.url, conf.getIP(), conf.getPort(), Addresses.BANK_POST.method)

    // creating default account
    val defaultAccount = Account(name = "default", money = 5000000, id = 1);
    var accounts = HashMap<String, Account>();
    accounts.put(defaultAccount.name, defaultAccount);


    val gson = Gson()
    var managePayments = ManagePayments(account = accounts, logger = conf.logger);
    var sendBankInfo = SendInfo(shuffleIp, shufflePort, conf.logger, gson.toJson(bank))

    // start listening with API
    var api = APIHandler(gson, accounts, managePayments, conf.logger, bank);
    api.initApi();

    // start registering bank in shuffler
    sendBankInfo.start();

}