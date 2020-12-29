package com.notemaster.android.ws.v1.notemasterweb.database;

import java.util.ArrayList;
import java.util.List;

public class Configurations {
	
	private List<Configuration> datasource = new ArrayList<Configuration>();

	public Configurations() {
		super();
	}

	public List<Configuration> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Configuration> datasource) {
		this.datasource = datasource;
	}

	public int getNumberOfConfigurations() {
		return datasource.size();
	}
	
	public Configuration getConfiguration(String type) {
		
		if(datasource.size() > 0) {
			for(Configuration c: datasource) {
				if(c.getType().equals(type)) {
					return c;
				}
			}
		}
		return null;
	}
	
}

