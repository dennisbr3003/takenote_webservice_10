package com.notemaster.android.ws.v1.notemasterweb.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSourceConfigurations {

	
	private Configuration readJsonDataSourceParameters(String databaseType) {
		
		String line;
		InputStream in;
		
		try {
			in = getClass().getResourceAsStream("datasource.config.json");				
		} catch(Exception e) {
			System.out.println("datasource.config.json (Exception) : " + e.getMessage());
			return null;
		} 
		
		StringBuilder str = new StringBuilder(); 
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			while((line = br.readLine()) != null){	
				str.append(line.replaceAll("\\s", ""));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Connection (FileNotFoundException) : " + e.getMessage());
			return null;			
		} catch (IOException e) {
			System.out.println("Connection (IOException) : " + e.getMessage());
			return null;
		} catch(Exception e) {
			System.out.println("Connection (Exception) : " + e.getMessage());
			return null;
		}	
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Configurations connectionParameters = mapper.readValue(str.toString(), Configurations.class);
			System.out.println(String.format("Found %s datasource configurations", String.valueOf(connectionParameters.getNumberOfConfigurations())));
			return connectionParameters.getConfiguration(databaseType);
		} catch (JsonProcessingException e) {
			System.out.println("Connection (JsonProcessingException) : " + e.getMessage());
			return null;
		}
	}
	
	private Configuration readCredentials(String databaseType) {
		
		Configuration configuration = new Configuration();

		String line;
		InputStream in;
		
		try {
			in = getClass().getResourceAsStream("datasource.config.txt");				
		} catch(Exception e) {
			System.out.println("datasource.config.txt (Exception) : " + e.getMessage());
			return null;
		} 
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			
			String stopCondition = "";

			while(((line = br.readLine()) != null) && !(line.equals(stopCondition))){

				String values[] = line.split("\\=");
				
				if(values.length == 2 && values[0].equals("TYPE") && values[1].equals(databaseType)) {
					stopCondition = "#";
				}				

				switch(values[0]) {
				case "TYPE":
					if(values.length == 2) { 
						configuration.setType(values[1]);
					} else {
						configuration.setType("");
					}
					break;
				case "USER":
					if(values.length == 2) {
						configuration.setUser(values[1]);
					}else {
						configuration.setUser("");
					}
					break;
				case "PWD":
					if(values.length == 2) {
						configuration.setPwd(values[1]);
					} else {
						configuration.setPwd("");
					}
					break;
				case"CATALOG":
					if(values.length == 2) {
						configuration.setCatalog(values[1]);
					} else {
						configuration.setCatalog("");
					}
					break;
				case "SERVER":
					if(values.length == 2) {
						configuration.setServer(values[1]);
					} else {
						configuration.setServer("");
					}
					break;
				case "DRIVER":
					if(values.length == 2) {
						configuration.setDriver(values[1]);
					} else {
						configuration.setDriver("");
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
		
		return configuration;
	}
	
	public Configuration createDataSourceCredentials(String databaseType) {

		try {
			return readJsonDataSourceParameters(databaseType);
		}catch (Exception e) {
			System.out.println("JSON: " + e.getMessage());
			return null;
		}
		
	}
	
}
