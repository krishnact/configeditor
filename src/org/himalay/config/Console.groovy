package org.himalay.config;

import org.himalay.commandline.AutoLogger
import org.himalay.commandline.CLTBaseQuiet
import org.himalay.commandline.Option
import org.slf4j.LoggerFactory

import groovy.json.JsonBuilder

public class Console implements AutoLogger{
	public static org.slf4j.Logger Logger = LoggerFactory.getLogger(Console.class)
	public void log (def msg) {
		if ( msg instanceof String || msg instanceof Long|| msg instanceof Integer|| msg instanceof Short|| msg instanceof Boolean ) {
			this.info(msg)
		}else {
			this.info(new JsonBuilder(msg).toPrettyString())
		}
	}
}
