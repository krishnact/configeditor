package org.himalay.config;

import org.himalay.commandline.AutoLogger
import org.himalay.commandline.Util
import org.slf4j.LoggerFactory

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import javafx.application.Platform

public class BrowserJavaPeer implements AutoLogger{

	public static org.slf4j.Logger log = LoggerFactory.getLogger(BrowserJavaPeer.class)
	Browser browser;
	
	
	public BrowserJavaPeer(Browser browser) {
		super();
		this.browser = browser;
	}
	public void init() {
		load();
	}
	
	
	private void deepIterate(def value, def parentObj,def formdef, Closure transform ) {
		for(name in value){
			def key = name;
			def inputId = name;
			if ( parentObj != ''){
				inputId = parentObj + '.' + inputId
			}
			def aVal = value[key]
			def formDefVal = formdef[inputId]
			if ( formDefVal == 'encrypted'){
				value[key] = transform(value[key])
			}
		}
	}
	
	
	
	private void load(){
		String confData = this.loadConf()
		JsonSlurper js = new JsonSlurper();
		debug "Loading ${confData}"
		def data = js.parseText(confData)
		
		def formdef = (this.browser.editor.getFormDef())
		if ( formdef == null){
			if ( data._formdef != null) {
				formdef = data._formdef
			}else {
				formdef = [:]
			}
		}
		
		def formData = [confData: confData, formdef : formdef]
		confData = new JsonBuilder(formData).toString()
		browser.setValue(confData)
	}
	
	
	public void save(String json) {
		//String confData = this.loadConf()
		JsonSlurper js = new JsonSlurper();
		def data = js.parseText(json)
		saveConf(new JsonBuilder(data.confData).toPrettyString())
	}
	
	public void exit() {
		info "Exiting"
		Platform.exit();
	}
	
	/**
	 *
	 * @param clear Clear text
	 * @return Base64 encoded bytes
	 */
	public String decrypt(String base64){
		return browser.editor.decrypt(base64);
	}
	
	
	public String encrypt(String clear){
		return browser.editor.encrypt(clear);
	}

	public String getTime() {
		String ret = ""+ new Date();
		return ret;
	}
	

	public String loadConf() {
		String dataText = '{}'
		if (this.browser.editor.opts.configJsonFile!=null) {
			dataText = this.browser.editor.processLoadingData(this.browser.editor.opts.configJsonFile.text);
		}else {
			log.info("Config file not avilable")
		}
		return dataText
	}
	
	private void saveConf(String confVal) {
		trace confVal
		this.browser.editor.opts.configJsonFile.text = this.browser.editor.processSavingData(confVal)
	}
}
