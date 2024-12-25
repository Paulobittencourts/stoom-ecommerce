package com.br.stoom.commerce.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommerceConfig {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
