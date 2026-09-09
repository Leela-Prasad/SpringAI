package com.mcp.springai_playground.sec01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SectionRunner {

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.host")
    static class Host {
        public static void main(String[] args) {
            SpringApplication.run(Host.class, "--section=sec01", "--config=host");
        }
    }

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.server")
    static class Server {
        public static void main(String[] args) {
            SpringApplication.run(Server.class, "--section=sec01", "--config=server");
        }
    }

}
