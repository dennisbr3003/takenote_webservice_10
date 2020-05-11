package com.notemaster.android.ws.v1.notemasterweb.response;

import java.util.ArrayList;
import java.util.List;

import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;

public class SharedPreferenceResponse {
    
	private String device_id;
    private List<ArrayItemObject> shared_preference = new ArrayList<ArrayItemObject>();


    public SharedPreferenceResponse() {
    	super();
    }

    public SharedPreferenceResponse(String device_id) {
    	super();
        this.device_id = device_id;
    }
    
    public SharedPreferenceResponse(String device_id, List<ArrayItemObject> shared_preference) {
    	super();
        this.device_id = device_id;
        this.shared_preference = shared_preference;
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

   public void addArrayElement(String item_id, String item_name, String item_value, String item_datatype) {
	   ArrayItemObject aio = new ArrayItemObject(item_id, item_name, item_value, item_datatype);
	   this.shared_preference.add(aio);
   }
    
}
