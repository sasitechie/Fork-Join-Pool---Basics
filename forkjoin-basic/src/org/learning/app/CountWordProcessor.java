package org.learning.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class CountWordProcessor extends RecursiveTask<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String path;
	private String word;
	
	public  CountWordProcessor(String path, String word) {
		this.path = path;
		this.word = word;
		
	}
	
	Integer totalCnt = new Integer(0);

	@Override
	protected Integer compute() {
		// get the list of files from the given path
		File[] root = new File(this.path).listFiles();
		List<File> rootFiles = Arrays.asList(root);
		
		//iterate the list
		rootFiles.forEach(file -> {
			// check if the list has any folder 
			if(file.isDirectory() && !file.toString().equals(this.path)){
				System.out.println("Directory ---> "+file);
				//if any folder is found then create a new thread 
				CountWordProcessor cnt  = new CountWordProcessor(file.toString(), this.word);
				//split that as a separate task to get the files inside that folder
				cnt.fork();
				// join the results of forked taks
				totalCnt += cnt.join();
				
			}
			else{
				if(file.isFile()){
					System.out.println("File ---> "+file);
					try {
						//if file is found then iterate the lines to count the word
						List<String> list = Files.lines(file.toPath(),  Charset.forName("Cp1252"))
								.filter(line -> line.contains(this.word))
								.collect(Collectors.toList());
						System.out.println("SIZE--> "+list.size());
						totalCnt += Integer.valueOf(list.size());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		return totalCnt;
	}

}
