package com.lightbend.lagom.recipes.cinnamon.hello.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.recipes.cinnamon.hello.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class HelloServiceImpl implements HelloService {

    private final Logger log = LoggerFactory.getLogger(HelloServiceImpl.class);


    private final HelloService helloService;

//    @Inject
//    public HelloServiceImpl() {
//
//    }

    @Inject
    public HelloServiceImpl(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return msg -> {
            log.info("hello: {}.", id);
            return completedFuture("Hello " + id);
        };
    }

    @Override
    public ServiceCall<NotUsed, String> helloProxy(String id) {
        return msg -> {
            log.info("helloProxy: {}.", id);
//            return CompletableFuture.completedFuture("Hello service said: " + id);
            CompletionStage<String> response = helloService.hello(id).invoke(NotUsed.getInstance());
            // currently Cinnamon doesn't propagate MDC context here as it doesn't support Java CompletionStage composition methods
            return response.thenApply(answer -> {
                log.info("thenApply {}", id);
                return "Hello service said: " + answer;
            }).thenComposeAsync(a -> {
                log.info("thenComposeAsync {}", id);
                return CompletableFuture.completedFuture(a);
            });
        };
    }

}
