package nutshell.server.feign.config;

import nutshell.server.ServerApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = ServerApplication.class)
@Configuration
public class FeignConfig {
}
