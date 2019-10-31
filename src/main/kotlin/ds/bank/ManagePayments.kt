package ds.bank

import ds.core.common.Logger
import ds.core.payment.Payment
import ds.core.payment.PaymentType
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Class with thread which process all payments in order
 * @param account hashmap of all accounts
 * @param logger instance of logger
 */
class ManagePayments (account: HashMap<String, Account>, logger: Logger) {

    private var accounts = account;
    var paymentsCount = 0;
    var payments = ArrayList<Payment>();
    var waitingForNumber = 0;
    private var thread : Thread;
    var shouldRun = false;
    var logger = logger;

    init {
        thread = threadDefine();

    }

    /**
     * defining thread
     */
    private fun threadDefine() : Thread {
        return Thread {
            while (true) {
                Thread.sleep(100);
                if (shouldRun) {
                    if (payments.isEmpty()) {
                        shouldRun = false;
                        continue;
                    }

                    while (payments.get(0).sequence == waitingForNumber) {
                        doPayment(payments.get(0));
                        paymentsCount++;
                        waitingForNumber++;
                        payments.removeAt(0);
                    }
                }
                shouldRun = false;
            }
        }
    }

    /**
     * checks if bank can do payment and prepares rest of payments
     * @param payment payment to process
     */
    fun manageArray(payment: Payment) {
        if (payment.sequence == waitingForNumber) {
            doPayment(payment);
            paymentsCount++;
            waitingForNumber++;

            if (payments.isNotEmpty()) {
                while (payments.get(0).sequence == waitingForNumber) {
                    doPayment(payments.get(0));
                    waitingForNumber++;
                    paymentsCount++;
                    payments.removeAt(0);
                    if (payments.isEmpty()) {
                        break;
                    }
                }
                shouldRun = false;
            }

        } else {
            payments.add(payment);
            payments.sortWith(Comparator { o1, o2 -> o1.sequence.compareTo(o2.sequence) });
        }
    }

    /**
     * does payment
     * @param payment payment to do
     */
    private fun doPayment(payment: Payment) {
        logger.logInfo("Doing payment : " + payment.sequence + " money> " + payment.money + " on account> " + payment.name + " ")
        var account = findAccountByName(payment.name);
        if (payment.type == PaymentType.CREDIT){
            doCreditPayment(account, payment.money);
        }else {
            doDebitPayment(account, payment.money);
        }
    }

    /**
     * add money to account
     * @param account account
     * @param money money to add
     */
    private fun doCreditPayment(account: Account, money : Int) {
        account.money += money;
        accounts.put(account.name, account);
    }

    /**
     * decrease money on account if can
     * @param account account
     * @param money money to take
     */
    private fun doDebitPayment(account: Account, money: Int) {
        if (account.money - money >= 0) {
            account.money -= money;
        }
        accounts.put(account.name, account);
    }

    /**
     * finding account by name
     * @param name name of account
     * @return account instance
     */
    private fun findAccountByName(name: String) : Account {
        return accounts.getValue(name);
    }
}