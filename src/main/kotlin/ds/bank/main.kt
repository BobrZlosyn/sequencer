import ds.bank.Account
import spark.Spark.*
import com.google.gson.Gson


fun main(args: Array<String>) {
    val defaultAccount = Account(name = "default", money = 5000000, id = 1);
    port(8084)
    val gson = Gson()
    get("/") { req, res -> "Banka vás vítá" }

    get("/money", "application/json", {
            request, response ->
        defaultAccount.money
    } , gson::toJson);


    patch("/bank/patch", "application/json", {
            request, response ->
        println(request.body());
        "ok"

    }, gson::toJson);

}