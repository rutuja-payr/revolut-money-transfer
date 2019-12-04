package com.revolut.transfers

import okhttp3.*
import spock.lang.Specification

class AppIntegrationTest extends Specification {
    final String BASE_URL = "http://localhost:4567/api/account"
    static OkHttpClient client

    def setupSpec() {
        App.main([] as String[])
        client = new OkHttpClient()
        sleep(15000)
    }

    def "create empty account is successful"() {
        setup:

        when: "adam creates new account"
        Response adamAccount = createAccount("{\"currency\":\"NOK\"}")
        then: "account is created with no found"
        adamAccount.code() == 201
        adamAccount.body().string().contains("\"balance\":{\"currency\":\"NOK\",\"amount\":0}")

        when: "beth creates new account"
        Response bethAccount = createAccount("{\"currency\":\"NOK\", \"initialBalance\":345.67}")
        then: "account is created with some initial founds"
        bethAccount.code() == 201
        bethAccount.body().string().contains("\"balance\":{\"currency\":\"NOK\",\"amount\":345.67}")

        when: "beth makes transfer to adam"
        Response transfer = makeTransfer(2, 1, "{\"currency\":\"NOK\",\"amount\":13.45}")
        then: "transfer is successful"
        transfer.code() == 204

        when: "adam makes transfer back to beth, he does not have enough founds"
        Response returnTransfer = makeTransfer(1, 2, "{\"currency\":\"NOK\",\"amount\":19.99}")
        then: "transfer is refused"
        returnTransfer.code() == 500

        when: "beth checks her account"
        Response bethAccountAfterTransfer = checkAccount(2)
        then: "transfer is successful"
        bethAccountAfterTransfer.code() == 200
        bethAccountAfterTransfer.body().string().contains("\"balance\":{\"currency\":\"NOK\",\"amount\":332.22}")

        when: "adam checks his account"
        Response adamAccountAfterTransfer = checkAccount(1)
        then: "transfer is successful"
        adamAccountAfterTransfer.code() == 200
        adamAccountAfterTransfer.body().string().contains("\"balance\":{\"currency\":\"NOK\",\"amount\":13.45}")

        when: "adam decides to delete its own account"
        Response adamDeleted = deleteAccount(1)
        then: "transfer is successful"
        adamDeleted.code() == 204
    }

    private Response createAccount(String json) {
        RequestBody body = jsonBody(json)
        def (Request request, Call call) = post(body)
        Response response = call.execute()
        response
    }

    private Response makeTransfer(Long fromId, Long toId, String json) {
        RequestBody body = jsonBody(json)
        def (Request request, Call call) = get(fromId, toId, body)
        Response response = call.execute()
        response
    }

    private Response checkAccount(Long id) {
        def (Request request, Call call) = get(id)
        Response response = call.execute()
        response
    }

    private Response deleteAccount(Long id) {
        def (Request request, Call call) = delete(id)
        Response response = call.execute()
        response
    }

    private RequestBody jsonBody(String json) {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json)
        body
    }

    private List post(RequestBody body) {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build()
        Call call = client.newCall(request)
        [request, call]
    }

    private List get(Long fromId, Long toId, RequestBody body) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + fromId + "/transferTo/" + toId)
                .post(body)
                .build()
        Call call = client.newCall(request)
        [request, call]
    }

    private List get(Long id) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .get()
                .build()
        Call call = client.newCall(request)
        [request, call]
    }

    private List delete(Long id) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build()
        Call call = client.newCall(request)
        [request, call]
    }
}
