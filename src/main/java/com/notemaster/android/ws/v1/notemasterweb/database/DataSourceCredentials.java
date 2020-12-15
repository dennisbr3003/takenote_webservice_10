package com.notemaster.android.ws.v1.notemasterweb.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataSourceCredentials {

	private Credentials readCredentials(String databaseType) {
		
		File MyTextFile = new File("config/datasource.config");
		String line;
		
		Credentials credentials = new Credentials();
		
		try(BufferedReader br = new BufferedReader(new FileReader(MyTextFile))) {
			
			String stopCondition = "";

			while(((line = br.readLine()) != null) && !(line.equals(stopCondition))){

				String values[] = line.split("\\=");
				
				if(values.length == 2 && values[0].equals("TYPE") && values[1].equals(databaseType)) {
					stopCondition = "#";
				}				

				switch(values[0]) {
				case "TYPE":
					if(values.length == 2) { 
						credentials.setType(values[1]);
					} else {
						credentials.setType("");
					}
					break;
				case "USER":
					if(values.length == 2) {
						credentials.setUser(values[1]);
					}else {
						credentials.setUser("");
					}
					break;
				case "PWD":
					if(values.length == 2) {
						credentials.setPwd(values[1]);
					} else {
						credentials.setPwd("");
					}
					break;
				case"CATALOG":
					if(values.length == 2) {
						credentials.setCatalog(values[1]);
					} else {
						credentials.setCatalog("");
					}
					break;
				case "SERVER":
					if(values.length == 2) {
						credentials.setUrl(values[1]);
					} else {
						credentials.setUrl("");
					}
					break;
				case "DRIVER":
					if(values.length == 2) {
						credentials.setDriver(values[1]);
					} else {
						credentials.setDriver("");
					}
					break;	
				}
			}	   

		} catch (FileNotFoundException e) {
			System.out.println("Credentials (FileNotFoundException) : " + e.getMessage());
			return null;			
		} catch (IOException e) {
			System.out.println("Credentials (IOException) : " + e.getMessage());
			return null;
		} catch(Exception e) {
			System.out.println("Credentials (Exception) : " + e.getMessage());
			return null;
		}
		
		return credentials;
	}
	
	public Credentials createDataSourceCredentials(String databaseType) {
		return readCredentials(databaseType);
	}
	
}
