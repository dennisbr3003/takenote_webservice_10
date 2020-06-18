package com.notemaster.android.ws.v1.notemasterweb.response;

import java.util.ArrayList;
import java.util.List;

import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.Image;
import com.notemaster.android.ws.v1.notemasterweb.payload.Note;

public class UserDataResponse {
    
	private String device_id;
    private List<ArrayItemObject> shared_preference = new ArrayList<ArrayItemObject>();
    private List<Note> noteList = new ArrayList<Note>();
    private List<Image> passPointImageList = new ArrayList<Image>();

    public UserDataResponse() {
    	super();
    }

    public UserDataResponse(String device_id) {
    	super();
        this.device_id = device_id;
    }
    
    public UserDataResponse(String device_id, List<ArrayItemObject> shared_preference) {
    	super();
        this.device_id = device_id;
        this.shared_preference = shared_preference;
    }
    
	public UserDataResponse(String device_id, List<ArrayItemObject> shared_preference, List<Note> noteList) {
		super();
		this.device_id = device_id;
		this.shared_preference = shared_preference;
		this.noteList = noteList;
	}

	public UserDataResponse(String device_id, List<ArrayItemObject> shared_preference, List<Note> noteList,
			List<Image> passPointImageList) {
		super();
		this.device_id = device_id;
		this.shared_preference = shared_preference;
		this.noteList = noteList;
		this.passPointImageList = passPointImageList;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public List<ArrayItemObject> getShared_preference() {
		return shared_preference;
	}

	public void setShared_preference(List<ArrayItemObject> shared_preference) {
		this.shared_preference = shared_preference;
	}
	
	
   public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
	}

	public List<Image> getPassPointImageList() {
		return passPointImageList;
	}

	public void setPassPointImageList(List<Image> passPointImageList) {
		this.passPointImageList = passPointImageList;
	}

	public void addSharedPreferenceArrayElement(String item_id, String item_name, String item_value, String item_datatype) {
		   ArrayItemObject aio = new ArrayItemObject(item_id, item_name, item_value, item_datatype);
		   this.shared_preference.add(aio);
	}
	
	//String id, String name, String created, String updated, byte[] file
	public void addNoteArrayElement(String item_id, String item_name, String item_created, String item_updated, byte[] item_file) {
		   Note note = new Note(item_id, item_name, item_created, item_updated, item_file);
		   this.noteList.add(note);
	}

	//String id, String name, String created, String updated, byte[] file
	public void addImageArrayElement(String item_id, String item_name, String item_created, String item_updated, byte[] item_file) {
		   Image image = new Image(item_id, item_name, item_created, item_updated, item_file);
		   this.passPointImageList.add(image);
	}	
	
}
