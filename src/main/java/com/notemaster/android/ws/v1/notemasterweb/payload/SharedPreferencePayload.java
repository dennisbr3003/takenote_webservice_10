package com.notemaster.android.ws.v1.notemasterweb.payload;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencePayload {

    private String device_id;
    private static List<ArrayItemObject> shared_preference = new ArrayList<ArrayItemObject>();


    public SharedPreferencePayload() {
    	super();
    }

    public SharedPreferencePayload(String device_id) {
    	super();
        this.device_id = device_id;
    }

    public SharedPreferencePayload(String device_id, List<ArrayItemObject> shared_preference) {
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Device identifier %s\n", getDevice_id()));
        sb.append(String.format("Object array size %s\n", shared_preference.size()));
        for(ArrayItemObject aio : shared_preference){
            sb.append(String.format("Name %s value %s\n", aio.getItem_name(), aio.getItem_value()));
        }
        return sb.toString();
    }
    

}
