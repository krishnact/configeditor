package org.himalay.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.himalay.commandline.CLTBase;
import org.himalay.commandline.Util;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
//import javafx.geometry.HPos;
//import javafx.geometry.VPos;
//import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
import javafx.stage.Stage;
//import oshi.SystemInfo;
 
 
public class Editor extends Application {
	public static org.slf4j.Logger log = LoggerFactory.getLogger(Editor.class);
	Opts opts;
    private Scene scene;
    static String[] origArgs = null;
    String encKey = null;
    Browser brs= null;
    public Opts getOpts() {
		return opts;
	}

	public void setOpts(Opts opts) {
		this.opts = opts;
	}

	public Browser getBrs() {
		return brs;
	}

	public void setBrs(Browser brs) {
		this.brs = brs;
	}

	public void beforeStart() {
		
	}
	
	public String getOnRunningScript() {
		return "";
	}
	
	@Override 
    public void start(Stage stage) {
	    this.opts = getOptions();
		beforeStart();
		if (this.brs == null) {
			postCreate();
		}

    	if ( opts.getEncryptionKey() != null){
    		this.encKey = opts.getEncryptionKey();
    	}else{
    		this.encKey = defaultEncKey();
    	}
    	//this.opts.exposeToJmx();
    	// create the scene
	    stage.setTitle(opts.getTitle());
        scene = new Scene(brs,opts.getWidth(),opts.getHeight(), Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }
    
    public Editor() {
    }
    
    public void postCreate() {
    	brs = new Browser(this);
    }
    
    public Map<String,Object> getWindowObjects() {
    	log.info("No additional objects being set.");
    	return new HashMap<String, Object>();
    }
    
    public String defaultEncKey(){
    	
    	return getCPUId();
    }
    public Opts getOptions() {
    	Opts tmp = new Opts();
    	CLTBase._main(tmp, origArgs);
    	return tmp;
    }
    
    public static void main(String[] args){
       _main(args);
    }
    
    public static void _main(String[] args){
        origArgs = args;
        launch(args);
    }

    
    public String processLoadingData(String dataFromLocalDisk) {
    	return dataFromLocalDisk;
    }
    
    public String processSavingData(String dataFromLocalForm) {
    	return dataFromLocalForm;
    }
    
    public URL getFormURL() {
    	try {
    		File htmlForm = opts.getHtmlForm();
    		if ( htmlForm.exists()) {
    			return this.opts.getHtmlForm().toURI().toURL();
    		}else {
    			throw new RuntimeException("HTML form not found: "+ htmlForm.getAbsolutePath());
    		}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    /**
     * 
     * @param clear Clear text
     * @return Base64 encoded bytes
     */
    public String decrypt(String base64){
    	String retVal = "";
    	try{
    		retVal = Util.decrypt(encKey, base64);
    		if ( retVal.startsWith("000000")) {
    			retVal = retVal.substring(6);
    		}else {
    			retVal = null;
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return retVal;
    }
    
    
    public String encrypt(String clear){
    	String retVal = Util.encrypt(encKey, "000000"+clear);
    	return retVal;
    }
    
	private static String getCPUId() {
		oshi.SystemInfo si = new oshi.SystemInfo();
		String procId = si.getHardware().getProcessor().getProcessorID() + "0123456789012345";
		return procId.substring(0, 15);
	}

	public Map<?,?> getFormDef(){
		if (this.opts.getFormDef() != null && this.opts.getFormDef().exists()){
			return (Map<?,?>)(new Util().getJsonConf(this.opts.getFormDef().getAbsolutePath()));
		}
		return null;
	}
}

