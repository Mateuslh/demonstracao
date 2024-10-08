package com.projetoDemonstracao.demonstracao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DemonstracaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemonstracaoApplication.class, args);
    }

}
