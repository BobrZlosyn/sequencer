package ds.shuffler

import ds.core.payment.Payment
import java.util.concurrent.Semaphore
import kotlin.collections.ArrayList

class Suffling {

    private var payments : ArrayList <Payment> = ArrayList<Payment>();
    private var semaphore : Semaphore = Semaphore(1);

    fun addToList(payment: Payment) {
        semaphore.acquire();
        payments.add(payment);
        semaphore.release();
    }

    fun suffleList() {
        semaphore.acquire();
        payments.shuffle();
        semaphore.release();
    }

    fun removeFromList(payment: Payment) {
        semaphore.acquire();
        payments.remove(payment)
        semaphore.release();
    }

    fun getCopy() : ArrayList <Payment> {
        semaphore.acquire();
        var temp = ArrayList <Payment>(payments);
        payments.clear();
        semaphore.release();
        return  temp;

    }


}