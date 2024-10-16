package com.nus.edu.se.user_service.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EurekaConfig {

  @Value("${server.port}")
  int serverPort;

  @Bean
  public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {

    EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
    String ip = null;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.error("Error at eurekaInstanceConfig", e);
    }
    config.setIpAddress(ip);
    config.setNonSecurePort(serverPort);
    config.setNonSecurePortEnabled(true);
    config.setPreferIpAddress(true);
    return config;
  }
}
