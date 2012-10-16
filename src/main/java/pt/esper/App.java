package pt.esper;

import java.util.ArrayList;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

//Main entry point
public class App 
{
	
	void initialize(String serviceName, String environment) throws Exception {
	    // Initialize the Spring Manager, singleton
	    SpringManager toSet = SpringManager.getInstance();
	    toSet.initialize();
	    
	}
	
	 void start() throws Exception {
		    // Start the Spring container and a Esper Service
		    SpringManager.getInstance().start();
		    System.out.println( "SpringManager Loaded Successfully");
		    
    }
	
	private void setRuntimeContext() {
	    //System.setProperty(ContextHelper.SERVER_CONTEXT_KEY, ServerInfo.getServerName());
	}
		
    public static void main( String[] args )
    {
    	App mainProc 				= new App();
    	String primaryEngineName 	= null;
    	String environment 			= null;
		SimpleLayout layout = new SimpleLayout();
		ConsoleAppender appender = new ConsoleAppender(layout);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel(Level.ERROR);

        System.out.println( "Starting Esper Loader" );
        
        try {
          mainProc.setRuntimeContext();
          mainProc.initialize(primaryEngineName, environment);
          mainProc.start();
          
        } catch (Throwable xThrowable) {
        	System.out.println( "" + xThrowable.getMessage());
	        xThrowable.printStackTrace();
        }
        
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //Workaround to maintain process alive
        //Don't understand why Beans do not stay alive
        synchronized (mainProc) {
        	try {
        		mainProc.wait();
        	} catch (InterruptedException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
		}
    }
}
