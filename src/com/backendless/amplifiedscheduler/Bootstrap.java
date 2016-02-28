
package com.backendless.amplifiedscheduler;

import com.backendless.Backendless;
import com.backendless.servercode.IBackendlessBootstrap;


public class Bootstrap implements IBackendlessBootstrap
{
            
  @Override
  public void onStart()
  {
    Backendless.setUrl( "https://api.backendless.com" );
    Backendless.initApp( "E9C78AF2-BDC4-C5FA-FF3F-DA79004B9200", "20AA2278-8DDD-60DC-FFD5-31858210B000","v1");

    // add your code here
  }
    
  @Override
  public void onStop()
  {
    // add your code here
  }
    
}
        