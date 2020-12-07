package com.notemaster.android.ws.v1.notemasterweb;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

class TestAddSharedPreferenceH2M {

	// this class tests adding a shared preference data item into a memory
	// based H2 database. It is created for this test and it will be lost (including
	// its data) after the test.
	
	String device_id = "2a2d4b0dae8e42bb";
	ISharedPreferenceTable sharedPreferenceTable;
	List<ArrayItemObject> shared_preference = new ArrayList<ArrayItemObject>();
	boolean isEmptyH2;
	ArrayItemObject aio;
		
	@BeforeAll
	static void setUpBeforeClass() throws Exception {	
		
		H2Database.getInstance().connect();
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
		H2Database.getInstance().disconnect();
		
	}

	@BeforeEach
	void setUp() throws Exception {
		
		DAOFactory factory = DAOFactory.getFactory(DAOFactory.H2M);
		
		// in case of a new empty database the tables are created automatically
		IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
		if(databaseBusinessObject.initDatabaseTables()) {
			System.out.println("Table definitions checked (and tables where added if missing)");
		}		
		
		sharedPreferenceTable = factory.getSharedPreferenceTable();
			
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {

		int t = 0;
		aio = new ArrayItemObject(device_id, "open_note8", "Dennis Brink", "String");
		
		System.out.println("Device ID (key): " + String.valueOf(aio.getItemId()));
		
		sharedPreferenceTable.insertRecord(aio);
		isEmptyH2 = sharedPreferenceTable.isEmpty(device_id);
		
		Assert.state(isEmptyH2 == false, "H2 table has not been updated");
		
		//Retrieve the preference
		UserDataResponse udr = new UserDataResponse();
		udr.setDevice_id(device_id);
				
		udr = sharedPreferenceTable.getSharedPreferenceResponse(udr);		
		
		shared_preference = udr.getShared_preference();
		System.out.println("Number of entries in list: " + String.valueOf(shared_preference.size()));
		
		
		for(ArrayItemObject x : shared_preference) {	
		    System.out.println("Preference name saved: " + String.valueOf(x.getItemName()));
			t++;
		}

		Assert.state(t == 1, "H2 table is not updated correctly, number of entries is incorrect");
		
	}

}
