package com.notemaster.android.ws.v1.notemasterweb.resource;

import java.util.UUID;

public class LoggerTakeNote extends Logger {

   private String device_id;
   private String transaction_id;
   
   public String getDevice_id() {
	   return device_id;
   }
   public void setDevice_id(String device_id) {
	   this.device_id = device_id;
	   loggingTable.setDevice_id(device_id);
   }
   public String getTransaction_id() {
	   return transaction_id;
   }
   public void setTransaction_id() {
	   this.transaction_id = UUID.randomUUID().toString();
	   loggingTable.setTransaction_id(this.transaction_id);
   }
   
}
