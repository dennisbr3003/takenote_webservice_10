package com.notemaster.android.ws.v1.notemasterweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.PSQLDatabase;
//import com.notemaster.android.ws.v1.notemasterweb.resource.InactivityScan;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;

@SpringBootApplication
public class NotemasterwebApplication {

	DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);
	
	public static void main(String[] args) {
		SpringApplication.run(NotemasterwebApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void InitWebService() {
		
		// start database here. It's too costly to do so at first use (start a connection when
		// the first request comes in) because the first request will time-out
		
		System.out.println("InitWebServiceDataSource");
		try {
			PSQLDatabase.getInstance().connect(); // singleton
			System.out.println("Database online and connection could be established");
		} catch (Exception e) {
			System.out.println("Database could not be connected " + e.getMessage());
		}

		// in case of a new empty database the tables are created automatically
		IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
		if(databaseBusinessObject.initDatabaseTables()) {
			System.out.println("Table definitions checked (and tables where added if missing)");
		}
		
		//PSQLDatabase.getInstance().disconnect();
		
		//start timer thread
		//InactivityScan inactivityScan = new InactivityScan();
		//inactivityScan.run();
	}	
	
}
