package com.dyteam.testApps.webserver.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {

    void init();

    void store(MultipartFile file, String dirLocation);

    Stream<Path> loadAll();

    Resource load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
