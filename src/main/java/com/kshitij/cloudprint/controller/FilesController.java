package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.ContentType;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FilesController {
    @Autowired
    GridFsOperations operations;
    @Autowired
    GridFSBucket bucket;
    @Autowired
    ContentType types;

    @RequestMapping(value = "/files/retrieve/{id}", method = RequestMethod.GET)
    public String retrieveFile(@PathVariable String id) {

        return "Hello";
    }

    @RequestMapping(value = "/files/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        ObjectId fileId;
        String ext = file.getOriginalFilename().split("\\.")[1];
        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(1024)
                .metadata(new Document("contentType", types.getContentType(ext)));
        try {

            System.out.println("Uploading file " + file.getOriginalFilename());
            fileId = bucket.uploadFromStream(file.getOriginalFilename(), file.getInputStream(), options);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error in uploading file", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(fileId.toString() + " file uploaded successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/files/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable String id) {
        bucket.delete(new ObjectId(id));
        return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
    }

}


//5ba23334820ace13ce22be8d