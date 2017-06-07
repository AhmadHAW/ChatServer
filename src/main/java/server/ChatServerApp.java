package server;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatServerApp {
	public static void main(String[] args) {
		SpringApplication.run(ChatServerApp.class, args);
		System.out.print("Der Server ist bereit.");
	}
}
