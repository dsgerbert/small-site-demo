package ru.h562.smallsite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.task.SmallSiteTask;

import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
@EnableScheduling
public class SmallSiteConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ConcurrentLinkedQueue<Message> messageQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public SmallSiteTask smallSiteTask() {
        return new SmallSiteTask();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
