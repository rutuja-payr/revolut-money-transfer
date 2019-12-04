package com.revolut.transfers;


import com.google.gson.Gson;
import com.revolut.transfers.account.config.TransfersConfig;
import com.revolut.transfers.account.domain.exceptions.AccountNotFoundException;
import com.revolut.transfers.account.infrastructure.TransferController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        TransferController controller = TransfersConfig.defaultTransfersConfig();
        Gson gson = TransfersConfig.gson();

        path("/api", () -> {
            before("/*", (request, response) -> log.info("Received api call"));
            before("/*", (request, response) -> response.type("application/json"));
            exception(AccountNotFoundException.class, (exception, request, response) -> {
                response.status(404);
                response.type("application/json");
                response.body("{\"message\":\"" + exception.getMessage() + "\"}");
            });
            post("/account/:id/transferTo/:transferToId", (Request request, Response response) -> {
                controller.makeTransfer(request, response);
                return "";
            });
            post("/account", (Request request, Response response) -> {
                return controller.createAccount(request, response);
            }, gson::toJson);
            get("/account/:id", (Request request, Response response) -> {
                return controller.retrieveAccount(request, response);
            }, gson::toJson);
            delete("/account/:id", (Request request, Response response) -> {
                controller.deleteAccount(request, response);
                return "";
            }, gson::toJson);
        });
    }

    private final static Logger log = LoggerFactory.getLogger(App.class);

}
