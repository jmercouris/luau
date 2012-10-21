package com.iit.luau;

import com.google.api.client.util.Key;

public class Place {
 
 @Key
 public String id;
  
 @Key
 public String name;
  
 @Key
 public String reference;
 
 @Key
 public String[] types;
 
 @Override
 public String toString() {
	 String type ="";
	 for(int i=0;i<types.length;i++){
		 type+=types[i]+ (i==types.length-1 ? "" : ", ");
	 }
  return "\nName: "+name + " - " + " Id: "  +id + " - Reference " + reference + " Types: "+ type;
 }
  
}