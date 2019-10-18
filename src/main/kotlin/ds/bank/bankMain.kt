import ds.bank.Account
import spark.Spark.*
import com.google.gson.Gson
import ds.bank.ManagePayments
import ds.core.Payment




fun main(args: Array<String>) {
    val defaultAccount = Account(name = "default", money = 5000000, id = 1);
    port(8084)
    ipAddress("192.168.1.110")
    val gson = Gson()
    get("/") { req, res -> "Banka vás vítá" }

    get("/money", "application/json", {
            request, response ->
        defaultAccount.money
    } , gson::toJson);

    var managePayments = ManagePayments(account = defaultAccount);
    patch("/bank/patch", "application/json", {
            request, response ->
        println(request.body());
        var paymentsRaw = request.body();
        var paymentArray = gson.fromJson(paymentsRaw, Array<Payment>::class.java)
        managePayments.manageArray(paymentArray);
        println(managePayments.account.money);

        "ok"

    }, gson::toJson);

}