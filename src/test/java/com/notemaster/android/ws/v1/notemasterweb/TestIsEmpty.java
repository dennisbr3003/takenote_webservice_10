package com.notemaster.android.ws.v1.notemasterweb;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.PSQLDatabase;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;

class TestIsEmpty {

	String device_id = "2a2d4b0dae8e42bb";
	boolean isEmptyH2;
	boolean isEmptyPSQL;
	ISharedPreferenceTable sharedPreferenceTable1;
	ISharedPreferenceTable sharedPreferenceTable2;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		H2Database.getInstance().connect();
		PSQLDatabase.getInstance().connect();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		H2Database.getInstance().disconnect();
		PSQLDatabase.getInstance().disconnect();
	}

	@BeforeEach
	void setUp() throws Exception {
		
		DAOFactory factory1 = DAOFactory.getFactory(DAOFactory.H2M);
		
		// in case of a new empty database the tables are created automatically
		IDatabaseBusinessObject databaseBusinessObject = factory1.getDatabaseBusinessObject();
		if(databaseBusinessObject.initDatabaseTables()) {
			System.out.println("Table definitions checked (and tables where added if missing)");
		}		
						
		sharedPreferenceTable1 = factory1.getSharedPreferenceTable();
		
		DAOFactory factory2 = DAOFactory.getFactory(DAOFactory.PSQL);
		sharedPreferenceTable2 = factory2.getSharedPreferenceTable();
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		isEmptyH2 = sharedPreferenceTable1.isEmpty(device_id);
		isEmptyPSQL = sharedPreferenceTable2.isEmpty(device_id);
		
		System.out.println(isEmptyH2);
		System.out.println(isEmptyPSQL);
		
        Assert.state(isEmptyH2 == true, "H2 database table is not empty");
        Assert.state(isEmptyPSQL == false, "PSQL database table should not be empty");
        
	}

}