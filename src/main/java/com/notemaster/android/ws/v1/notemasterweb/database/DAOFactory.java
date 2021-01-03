package com.notemaster.android.ws.v1.notemasterweb.database;

import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ILoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IUserTable;

public abstract class DAOFactory {

	public static final int PSQL = 0;
	public static final int H2F = 1;
	public static final int H2M = 2;
	
	public abstract IDatabaseBusinessObject getDatabaseBusinessObject();
	
	public abstract IGraphicTable getImageTable();
	public abstract ILoggingTable getLoggingTable();
	public abstract INoteTable getNoteTable();
	public abstract ISharedPreferenceTable getSharedPreferenceTable();
	public abstract IUserTable getUserTable();
	
	public static DAOFactory getFactory(int type){
		switch(type){
		case 0: return new PSQLDAOFactory();
		case 1: return new H2DAOFactory();
		case 2: return new H2DAOFactory(); // they are the same
		default: return null;
		}
	}
	
}
