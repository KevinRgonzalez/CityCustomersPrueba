package com.ts4.customer.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import com.ts4.customer.data.exception.CustomErrorDecoder;

import feign.codec.ErrorDecoder;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class InitApplication {
	public static void main(String[] args) {
		SpringApplication.run(InitApplication.class, args);
	}
}
