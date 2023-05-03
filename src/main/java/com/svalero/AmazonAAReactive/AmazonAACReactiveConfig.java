package com.svalero.AmazonAAReactive;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonAACReactiveConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
