package com.notemaster.android.ws.v1.notemasterweb.database;

import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ILoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.PSQLImageTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.PSQLLoggingTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.PSQLNoteTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.PSQLSharedPreferenceTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.PSQLUserTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IUserTable;

public class PSQLDAOFactory extends DAOFactory {
	
	public IDatabaseBusinessObject getDatabaseBusinessObject() {
		return new PSQLDatabaseBusinessObject();
	}

	@Override
	public IGraphicTable getImageTable() {
		return new PSQLImageTableDAO();
	}

	@Override
	public ILoggingTable getLoggingTable() {
		return new PSQLLoggingTableDAO();
	}

	@Override
	public INoteTable getNoteTable() {
		return new PSQLNoteTableDAO();
	}

	@Override
	public ISharedPreferenceTable getSharedPreferenceTable() {
		return new PSQLSharedPreferenceTableDAO();
	}

	@Override
	public IUserTable getUserTable() {
		return new PSQLUserTableDAO();
	}

}
