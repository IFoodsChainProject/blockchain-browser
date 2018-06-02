package com.ifoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.xnio.Options;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;

@SpringBootApplication
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory =
                new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
            @Override
            public void customize(Undertow.Builder builder) {
                builder.setSocketOption(
                        UndertowOptions.IDLE_TIMEOUT, 10000)
                        .setSocketOption(Options.REUSE_ADDRESSES, true)
                        .setSocketOption(Options.READ_TIMEOUT, 10000)
                        .setSocketOption(Options.WRITE_TIMEOUT, 10000)
                        .setSocketOption(Options.KEEP_ALIVE, true);

            }
        });
        return factory;
    }
}