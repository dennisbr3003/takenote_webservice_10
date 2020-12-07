package com.notemaster.android.ws.v1.notemasterweb.database;

import com.notemaster.android.ws.v1.notemasterweb.database.tables.H2ImageTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.H2NoteTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.H2SharedPreferenceTableDAO;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ILoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;

public class H2DAOFactory extends DAOFactory {
	public IDatabaseBusinessObject getDatabaseBusinessObject() {
		return new H2DatabaseBusinessObject();
	}

	@Override
	public IGraphicTable getImageTable() {
		return new H2ImageTableDAO();
	}

	@Override
	public ILoggingTable getLoggingTable() {
		//return new H2LoggingTableDAO();
		return null;
	}

	@Override
	public INoteTable getNoteTable() {
		return new H2NoteTableDAO();
	}

	@Override
	public ISharedPreferenceTable getSharedPreferenceTable() {
		return new H2SharedPreferenceTableDAO();
	}

}
