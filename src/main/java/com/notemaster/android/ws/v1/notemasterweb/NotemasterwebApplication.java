package com.notemaster.android.ws.v1.notemasterweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.notemaster.android.ws.v1.notemasterweb.h2_database.Database;

@SpringBootApplication
public class NotemasterwebApplication {

	private Database h2db = new Database();	
	
	public static void main(String[] args) {
		SpringApplication.run(NotemasterwebApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void InitWebServiceDataSource() {
		
		System.out.println("InitWebServiceDataSource");
		try {					
			if(h2db.initDatabase()) {
				System.out.println("Database online and connection could be established");
			} else {
				System.out.println("No database could be found and/or created, database may also be in use");
			}
		}catch(Exception e) {
			System.out.println("No database could be found and/or created");
		}
	}	
	
}
