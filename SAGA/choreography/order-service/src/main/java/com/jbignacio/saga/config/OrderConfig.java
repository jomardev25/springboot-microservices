package com.jbignacio.saga.config;


import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jbignacio.saga.events.order.OrderEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class OrderConfig {

	@Bean
    public Sinks.Many<OrderEvent> orderSink(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }

	@Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink){
        return sink::asFlux;
    }

}
