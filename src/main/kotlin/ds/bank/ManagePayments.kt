package ds.bank

import ds.core.payment.Payment
import ds.core.payment.PaymentType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ManagePayments (account: HashMap<String, Account>) {

    var accounts = account;
    var paymentsCount = 0;

    fun manageArray(payments: Array <Payment>) {
        var array = ArrayList<Payment>(payments.toMutableList())
        array.sortWith(Comparator { o1, o2 -> o1.sequence.compareTo(o2.sequence) });
        array.forEach {
            payment ->
                var account = findAccountByName(payment.name);
                if (payment.type.equals(PaymentType.CREDIT) || account.money - payment.money >= 0) {
                    account.money -= payment.money;
                }
                accounts.put(account.name, account);

            paymentsCount++;
        }
    }

    fun findAccountByName(name: String) : Account {
        return accounts.getValue(name);
    }
}