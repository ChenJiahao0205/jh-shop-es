package pers.jhshop.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@MapperScan(basePackages = "pers.jhshop.es.mapper")
//@EnableFeignClients(basePackages = "pers.jhshop.fapi")
@EnableDiscoveryClient
@SpringBootApplication
public class JhShopEsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JhShopEsServiceApplication.class, args);
    }

}
