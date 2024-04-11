package com.web.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {

	//docker run --name mysql -v /Users/hariduddukunta/MyWork/Softwares/MySqlData/:/var/lib/mysql --network eureka-network -e MYSQL_ROOT_PASSWORD=dudkrish1A -d -p 3306:3306 mysql:8

	//https://javatute.com/spring/transactional-required-vs-requires_new-example-in-spring-boot/
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
