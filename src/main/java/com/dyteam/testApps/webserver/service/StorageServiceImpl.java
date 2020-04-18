package com.dyteam.testApps.webserver.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling saving the file on the server side on request from browser.
 * @author deepak
 *
 */
@Service
public class StorageServiceImpl implements IStorageService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Path rootLocation;
	
	public StorageServiceImpl(@Value( "${testcase.data.file.basePath}" )String fileStorageBasePath) {
		this.rootLocation=Paths.get(fileStorageBasePath);
	}
	
	@Override
	public void init() {
		try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!",e);
        }
	}

	@Override
	public void store(MultipartFile file,String dirLocation) {
		try {
			Path path = Paths.get(dirLocation);
				logger.info("Creating parent dirs at ="+path.getParent());
				Files.createDirectories(path.getParent());
			String fileExtension = getFileExtensionWithDot(file.getOriginalFilename());
            Path resolve = path.getParent().resolve(path.getFileName()+fileExtension);
            logger.info("Creating file at ="+resolve);
			Files.copy(file.getInputStream(), resolve);
        } catch (Exception e) {
        	logger.error("Error occure while uploading file="+file.getName(),e);
        	throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
	}

	@Override
	public Stream<Path> loadAll() {
		try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
        	logger.error("Error occure while loading all files. ",e);
        	throw new RuntimeException("\"Failed to read stored file");
        }
	}

	@Override
	public Resource load(String filename) {
		try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
        	logger.error("Error occure while loading file="+filename,e);
        	throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
        	logger.error("Error occure while loading file="+filename,e);
        	throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
	}
	
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	
	private static String getFileExtensionWithDot(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf("."));
        else return "";
    }

}
