package org.himalay.config;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject

import org.himalay.commandline.AutoLogger
import org.slf4j.LoggerFactory

import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker.State
import javafx.geometry.HPos;
import javafx.geometry.VPos;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
//import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
import groovy.cli.commons.CliBuilder;
import groovy.cli.commons.OptionAccessor;
public class Browser extends Region implements AutoLogger {
	public static org.slf4j.Logger Logger = LoggerFactory.getLogger(Browser.class)
	final WebView webview = new WebView();
	final WebEngine webEngine = webview.getEngine();
	BrowserJavaPeer bp =new BrowserJavaPeer(this);
	final Console console =new Console();
	boolean loaded = false;
	Editor editor;
	public Browser(Editor editor) {
		info "Creating browser"
		
		this.editor = editor
		//apply the styles
		getStyleClass().add("browser");
		// load the web page
		String urlStr = editor.formURL
		// process page loading
		webEngine.getLoadWorker().stateProperty().addListener(
				new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							JSObject win = (JSObject) webEngine.executeScript("window");
							//bp.load();
							if ( editor.opts.startDebugger){
								webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
							}
							Platform.runLater(new Runnable() {
										@Override public void run() {
											info "Browser ready now"
											//println "Set java app object"
											addAppObjects();
											bp.init();
										}
									});

						}
					}
				}
				);
		webEngine.debugger.setEnabled(true)
		addAppObjects();
		webEngine.load(urlStr)
		//add the web view to the scene
		getChildren().add(webview);

	}

	private void addAppObjects(){
		JSObject win = (JSObject) webEngine.executeScript("window");
		bp =new BrowserJavaPeer(bp.browser);
		debug "Adding console and app"
		win.setMember("app", bp);
		win.setMember("console", console);

	}
//		def result = webEngine.executeScript('''
//			window.app_hello = function(valuesJson){
//				debug(valuesJson)
//				var values = JSON.parse(valuesJson)
//				var confData = JSON.parse(values.value)
//				for(name in confData){
//					document.getElementById(name).value = confData[name]
//				}
//				return window
//				}
//			''');
//			
//			println result;
//	}
	
	/**
	 * Set a string
	 * @param values
	 */
	public void setValue(String value) {
		def data = [value: StringEscapeUtils.escapeJavaScript(value), length : value.length(), mimeType: 'text/plain']
		String json = new JsonBuilder(data).toString()
		String jsScript = "window.app_loadData('${json}')"
		trace jsScript
		webEngine.executeScript(jsScript);
	}

	@Override protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(webview,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
	}

	@Override protected double computePrefWidth(double height) {
		return this.editor.opts.height;
	}

	@Override protected double computePrefHeight(double width) {
		return this.editor.opts.width;
	}
}