package com.ts4.customer.data.utils.logs;

import com.ts4.customer.data.exception.CustomErrorDecoder;
import feign.Client;
import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public Client cleinte() {
        return new OkHttpClient();
    }

    @Autowired
    private Tracer tracer;

    @Bean
    public Client feignClient() {
        return new FeignResponseInterceptor(cleinte(), tracer);
    }

    @Bean
    public Feign.Builder feignBuilder(Client feignClient) {
        return Feign.builder().client(feignClient);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}