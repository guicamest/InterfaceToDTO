package com.sleepcamel.ifdtoutils;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generates DTOS for interfaces with @ToDTO annotation
 *
 * @goal java
 * 
 * @phase process-classes
 */
public class MvnPluginMojo extends AbstractMojo
{
    /**
     * Location of target/classes
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File outputDirectory;
    
    private List<Class<?>> dtosToGenerate = new ArrayList<Class<?>>();

    @SuppressWarnings("deprecation")
	public void execute() throws MojoExecutionException
    {
    	try {

    	if ( outputDirectory == null || !outputDirectory.exists() ){
    		throw new MojoExecutionException("Class directory must exist");
   		}

		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {outputDirectory.toURL()},getClass().getClassLoader());
		Thread.currentThread().setContextClassLoader(urlClassLoader);

    	Collection<File> listFiles = FileUtils.listFiles(outputDirectory, new String[]{"class"}, true);
    	for(File file:listFiles){
    		getLog().debug("\nClass found: "+file.getName());
    		try {
    			Class<?> loadedClass = urlClassLoader.loadClass(fileToClass(outputDirectory, file));
				getLog().debug("Class "+loadedClass.getName()+ " loaded");
				if ( !loadedClass.isInterface() ){
					getLog().debug("Not an interface :(");
					continue;
				}
				
				ToDTO annotation = loadedClass.getAnnotation(ToDTO.class);
				
				if ( annotation == null ){
					getLog().debug("No annotation not found :(");
				}else{
					dtosToGenerate.add(loadedClass);
					getLog().debug("Annotation found! Package of creation: "+annotation.packageSuffix());
				}
			} catch (ClassNotFoundException e) {
			}
    	}
    	
    	getLog().info("Found "+dtosToGenerate.size()+" interfaces to generate their DTOs...\n");
    	
    	for(Class<?> interfaceClass:dtosToGenerate){
    		String packageSuffix = interfaceClass.getAnnotation(ToDTO.class).packageSuffix();
    		try{
				Thread.currentThread().getContextClassLoader().loadClass(DTOClassGenerator.getDTOName(interfaceClass,packageSuffix));
				getLog().info("DTO for interface "+interfaceClass.getName()+" exists, skipping generation\n");
    		}catch(ClassNotFoundException e){
    			getLog().info("Generating dto for interface "+interfaceClass.getName()+"...");
				DTOClassGenerator.generateDTOForInterface(interfaceClass, packageSuffix, outputDirectory.getAbsolutePath());
				getLog().info("DTO generated for interface "+interfaceClass.getName()+"\n");
			}
    	}

		} catch (Exception e1) {
			throw new MojoExecutionException(e1.getMessage());
		}

    }

	private String fileToClass(File baseDirectory, File file) {
		String classFilePath = FilenameUtils.removeExtension(file.getPath()).replace(baseDirectory.getPath(), "").substring(1);
		String className = classFilePath.replace(File.separatorChar, '.');
		while( classFilePath.length() != className.length() ){
			classFilePath = className;
			className = classFilePath.replace(File.pathSeparatorChar, '.');
		}
		return className;
	}
}
