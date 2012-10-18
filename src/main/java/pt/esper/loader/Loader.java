package pt.esper.loader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

import pt.esper.listener.DebugEventListener;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.annotation.Tag;
import com.espertech.esper.client.deploy.DeploymentException;
import com.espertech.esper.client.deploy.DeploymentOptions;
import com.espertech.esper.client.deploy.DeploymentOrder;
import com.espertech.esper.client.deploy.DeploymentOrderOptions;
import com.espertech.esper.client.deploy.DeploymentResult;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;
import com.espertech.esper.client.deploy.ParseException;

public class Loader {

    private String configRootDir;
    private String engineId;
    private EPServiceProvider engineServiceProvider;
    private ArrayList<String> eplFiles;
    private EPRuntime runtime;
    
    public Loader()
    {
        this.engineId = "tester";
    }
    
    /**
     * @return the eplFiles
     */
    public ArrayList<String> getEplFiles() {
        return eplFiles;
    }

    /**
     * @param eplFiles the eplFiles to set
     */
    public void setEplFiles(ArrayList<String> eplFiles) {
        this.eplFiles = eplFiles;
    }
    
    public void setupEngine()
    {
        Configuration engineConfiguration = new Configuration();
        File config = new File("src/main/resources/config/esper.xml");
        engineConfiguration.configure(config);
        engineConfiguration.getEngineDefaults().getExecution().setPrioritized(false);
        
        this.engineServiceProvider = EPServiceProviderManager.getProvider("Esper", engineConfiguration);
        EPAdministrator administrator           = this.engineServiceProvider.getEPAdministrator();
        EPDeploymentAdmin deployAdmin           = administrator.getDeploymentAdmin();
        this.runtime                            = this.engineServiceProvider.getEPRuntime();
        ArrayList<Module> modules = new ArrayList<Module>();
        modules = checkEpl(this.eplFiles, deployAdmin);
        DeploymentOrder order = null;
        
        try {
            order = deployAdmin.getDeploymentOrder(modules, new DeploymentOrderOptions());
        } catch (DeploymentException e) {
            System.out.println("Exception while parsing epl file : " + e.toString());
        }
        
        
        for (Module t : order.getOrdered())
        {
            DeploymentResult depRes = null;
            
            try {
                depRes = deployAdmin.deploy(t, new DeploymentOptions());
            } catch (DeploymentException e) {
                System.out.println("EPL Deployment Exception: " + e.toString());
            }
            
            for (EPStatement st : depRes.getStatements())
            {
                this.handleTags(st);
            }
        }
        
        
        System.out.println("Engine setup.");
        //this.runtime.getDataFlowRuntime().instantiate("StockFlow").run();
        
    }
    
    public void initialize() {
        //If no configuration directory given assume the usual Pulso dir
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = classLoader.getResource("").getPath();        
        System.out.println("Esper::EngineFromBus");
        System.out.println("Esper::EngineID -> " + this.engineId);
        ArrayList<String> fullEplFiles = new ArrayList<String>();
        
        for(String eplFile : this.eplFiles)
        {
            File dir = new File(path + eplFile);

            String[] children = dir.list();
            
            if (children == null) {
                //Either dir does not exist or is not a directory
                //Assuming that is an existing epl file
                System.out.println("Esper::See epl file -> " + eplFile);
                fullEplFiles.add(classLoader.getResource(eplFile).getPath());
            } else {
                for (int i=0; i<children.length; i++) {
                    // Get filename of file or directory
                    if(children[i].endsWith("epl")) {
                        System.out.println("Esper::See epl file from dir -> " + children[i]);
                        fullEplFiles.add(path + "/" + eplFile + "/" + children[i]);
                    }
                }
            }
        }
        
        this.eplFiles = fullEplFiles;
                
        this.setupEngine();
    }

    public void handleTags(EPStatement statement)
    {
        for (Annotation t : statement.getAnnotations())
        {
            if (t instanceof Tag) {
                Tag tag = (Tag) t;
                //System.out.println("Tag name " + tag.name() + " value " + tag.value());
                if(tag.name().equals("debug") && tag.value().equals("true"))
                {
                    System.out.println("Debug event listener: " + statement.getName());
                    statement.addListener(new DebugEventListener());
                }
                        
              }
        }
    }
    
    public static ArrayList<Module> checkEpl(ArrayList<String> eplFiles, EPDeploymentAdmin deployAdmin) {

        ArrayList<Module> modules = new ArrayList<Module>();
        try {
            for(String epl : eplFiles) {
        File f = new File(epl);
                modules.add(deployAdmin.read(f));
      }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Exception while opening file : " + e.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.println("Exception while parsing epl file : " + e.toString());
        }
        return modules;
    }

    public void sendEvent(HashMap event, String eventName)
    {
        this.runtime.sendEvent(event, eventName);
    }

    public void shutdown() {
        
    }
    
}
