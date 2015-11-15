void setup() {
  Serial.begin(19200);
  pinMode(13, OUTPUT);
  pinMode(8, OUTPUT);

  
  tone(8, 500, 1000);

}


void loop() {

  
    String bytes;
    bytes = Serial.readString();
    if(bytes.equals("")){
      Serial.print("no data");

      //signify string ended.
      Serial.print("END");
      Serial.println();
    }else{
      if(bytes.indexOf("FREQ-") >=0){
      String sub = bytes.substring(5);      
      int freq = sub.toInt();
      if(freq>0){

        
      tone(8, freq, 1000);

      
      }
      

      
      }
      
      if(bytes.equals("LED-ON")){
        Serial.print("Led going ON ");
        digitalWrite(13, HIGH);
      }

      if(bytes.equals("LED-OFF")){
        Serial.print("Led going OFF ");
        digitalWrite(13, LOW);
      }
      
      Serial.print(bytes);
      Serial.print("END");
      Serial.println();
    }
    
    //delay(10);

}
