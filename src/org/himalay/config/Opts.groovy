package org.himalay.config;

import org.himalay.commandline.CLTBase
import org.himalay.commandline.CLTBaseQuiet
import org.himalay.commandline.ExposeToJmx
import org.himalay.commandline.Option

import java.lang.management.ManagementFactory
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Method

import javax.management.Attribute
import javax.management.AttributeList
import javax.management.AttributeNotFoundException
import javax.management.DescriptorKey
import javax.management.DynamicMBean
import javax.management.InvalidAttributeValueException
import javax.management.MBeanAttributeInfo
import javax.management.MBeanException
import javax.management.MBeanInfo
import javax.management.MBeanOperationInfo
import javax.management.MBeanServer
import javax.management.ObjectName
import javax.management.ReflectionException
import groovy.cli.commons.CliBuilder;
import groovy.cli.commons.OptionAccessor;

public class Opts extends CLTBaseQuiet{

	@Option
	int width = 800
	
	@Option
	int height = 600;
	
	@Option
	File htmlForm
	
	@Option(required = true)
	File configJsonFile

	
	@Option
	String title ="Editor"
	
//	@Option
//	String color = "#666970"
	
	@Option
	String encryptionKey;
	
	@Option(numberOfOptions= 0)
	boolean startDebugger = false
	
	@Option
	File formDef 
	
	@Override
	protected void realMain(OptionAccessor options) {
	}
	
	@ExposeToJmx
	public void systemExit(int afterMs, int exitCode){
		
		info "Exiting after ${afterMs} ms with exit code ${exitCode}"
		if (afterMs > 0){
			Thread.sleep(afterMs)
		}
		System.exit(exitCode)
	}
	
	@Override
	public CliBuilder getCliBuilder() {
		groovy.cli.commons.CliBuilder retVal = super.getCliBuilder();
		retVal.setUsage("usage:java -jar configeditor-0.0.1-SNAPSHOT-shaded.jar <options> args");
		return retVal;
	}	
}