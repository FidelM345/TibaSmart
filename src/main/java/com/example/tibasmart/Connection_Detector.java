package com.example.tibasmart;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class Connection_Detector {
     Context context;


    public Connection_Detector(Context context) {

        this.context=context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

          if(connectivity!=null){

              NetworkInfo info=connectivity.getActiveNetworkInfo();

              if(info !=null){

                  if(info.getState()== NetworkInfo.State.CONNECTED){

                      try {
                          if(isInternetConnected()){

                              return true;


                          }
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }


                  }
              }
          }
        return false;
    }


    public boolean isInternetConnected() throws InterruptedException, IOException
    {
        String command = "ping -i 1 -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }



}
