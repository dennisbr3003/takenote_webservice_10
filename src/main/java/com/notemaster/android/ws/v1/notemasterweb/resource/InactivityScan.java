package com.notemaster.android.ws.v1.notemasterweb.resource;

import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.PSQLDatabase;

public class InactivityScan extends Thread {

	private volatile boolean kill_switch = true; 
	
	private static InactivityScan instance = new InactivityScan();
	// private constructor makes it a singleton
	private InactivityScan() {
	}	
	
	// static method can be used without instantiating the class
	public static InactivityScan getInstance() {
		return instance;
	}	
	
	public boolean getKill_switch() {
		return kill_switch;
	}

	public void setKill_switch(boolean kill_switch) {
		this.kill_switch = kill_switch;
	}

	@Override
	public void run() {

		super.run();

		while(kill_switch == false) {

			if ((Session.getInstance().getLastCallTimeStamp() / 1000) > 0 && 
					Math.abs((Session.getInstance().getLastCallTimeStamp() / 1000) - (System.currentTimeMillis() / 1000)) > 300) {
				try {
					if(PSQLDatabase.getInstance().verifyConnection()){
						PSQLDatabase.getInstance().disconnect();
						System.out.println("Closed PSQL connection due to inactivity");
					}
					if(H2Database.getInstance().verifyConnection()) {
						H2Database.getInstance().disconnect();
						System.out.println("Closed H2 connection due to inactivity");
					}			
					// stop scanning for unused connection that are closed
					System.out.println("Shutting down inactivity scan; all connections are closed");
					kill_switch = true;					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(5000L); // check every 5 second. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
