package com.notemaster.android.ws.v1.notemasterweb.payload;

import java.util.ArrayList;
import java.util.List;

public class UserDataPayload {

    private String device_id;
    private static List<ArrayItemObject> shared_preference = new ArrayList<ArrayItemObject>();
    private static List<Note> noteList = new ArrayList<Note>();
    private static List<Image> passPointImageList = new ArrayList<Image>();

    public UserDataPayload() {
    	super();
    }

    public UserDataPayload(String device_id) {
    	super();
        this.device_id = device_id;
    }

    public UserDataPayload(String device_id, List<ArrayItemObject> shared_preference) {
    	super();
        this.device_id = device_id;
        UserDataPayload.shared_preference = shared_preference;
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
        UserDataPayload.shared_preference = shared_preference;
    }   
    
    public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		UserDataPayload.noteList = noteList;
	}

	public List<Image> getPassPointImageList() {
		return passPointImageList;
	}

	public void setPassPointImageList(List<Image> passPointImageList) {
		UserDataPayload.passPointImageList = passPointImageList;
	}

	public int getPassPointImageListSize() {
    	if (UserDataPayload.passPointImageList != null) {
    		return UserDataPayload.passPointImageList.size();
    	} else {
    		return 0;
    	}
    }	

	public int getNoteListSize() {
    	if (UserDataPayload.noteList != null) {
    		return UserDataPayload.noteList.size();
    	} else {
    		return 0;
    	}
    }		

    public Note getNoteListElement(int idx) {
    	if ((UserDataPayload.noteList != null) && !(idx > getNoteListSize())) {    	
    	    return UserDataPayload.noteList.get(idx);
    	} else {
    		return null;
    	}    	
    }	
	
	public int getShared_preferenceSize() {
    	if (UserDataPayload.shared_preference != null) {
    		return UserDataPayload.shared_preference.size();
    	} else {
    		return 0;
    	}
    }
    
    public ArrayItemObject getShared_preferenceElement(int idx) {
    	if ((UserDataPayload.shared_preference != null) && !(idx > getShared_preferenceSize())) {    	
    	    return UserDataPayload.shared_preference.get(idx);
    	} else {
    		return null;
    	}    	
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Device identifier %s\n", getDevice_id()));
        sb.append(String.format("Object array size %s\n", shared_preference.size()));
        for(ArrayItemObject aio : shared_preference){
            sb.append(String.format("Name %s value %s\n", aio.getItemName(), aio.getItemValue()));
        }
        return sb.toString();
    }
    

}
