package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.ContentType;
import com.kshitij.cloudprint.configuration.CloudPrintProperties;
import com.kshitij.cloudprint.configuration.EncryptionHelper;
import com.kshitij.cloudprint.retrofit.AuthApi;
import com.kshitij.cloudprint.retrofit.SubmitOutput;
import com.mongodb.client.gridfs.GridFSBucket;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@RestController
public class PrintController {
    @Autowired
    CloudPrintProperties properties;
    @Autowired
    ContentType type;
    @Autowired
    AuthApi api;
    @Autowired
    GridFSBucket bucket;
    @Autowired
    EncryptionHelper encryptionHelper;

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity submit(@RequestParam("file") MultipartFile file) throws IOException {
        String name[] = file.getOriginalFilename().split("\\.");
        String ticket = "{\n" +
                "  \"version\": \"1.0\",\n" +
                "  \"print\": {\n" +
                "    \"vendor_ticket_item\": [],\n" +
                "    \"color\": {\"type\": \"STANDARD_MONOCHROME\"},\n" +
                "    \"copies\": {\"copies\": 1}\n" +
                "  }\n" +
                "}";
        /*GridFSDownloadStream downloadStream = bucket.openDownloadStream("QT Cheatsheet.pdf");
        int length = (int) downloadStream.getGridFSFile().getLength();
        byte[] content = new byte[length];
        downloadStream.read(content);
        downloadStream.close();*/
        RequestBody printerIdPart = RequestBody.create(MultipartBody.FORM, properties.getPrinterId());
        RequestBody titlePart = RequestBody.create(MultipartBody.FORM, name[0]);
        RequestBody ticketPart = RequestBody.create(MultipartBody.FORM, ticket);
        RequestBody contentTypePart = RequestBody.create(MultipartBody.FORM, type.getContentType(name[1]));
        RequestBody contentPart = RequestBody.create(
                okhttp3.MediaType.parse(file.getContentType()),
                file.getBytes()
        );
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("content", name[0], contentPart);

        Call<SubmitOutput> call = api.submitJob(
                printerIdPart,
                titlePart,
                ticketPart,
                filePart,
                contentTypePart
        );

        Response<SubmitOutput> response = call.execute();
        if (response.isSuccessful())
            return ResponseEntity.ok(response.body());
        else
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.raw().toString());
       /* call.enqueue(new Callback<SubmitOutput>() {
            @Override
            public void onResponse(Call<SubmitOutput> call, Response<SubmitOutput> response) {
                System.out.println("onResponse() called");
                System.out.println(new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<SubmitOutput> call, Throwable throwable) {
                System.out.println(throwable.toString());
            }
        });*/
    }

    @PostMapping("/signup")
    public void signup(@RequestParam("credentials") String credentials) throws Exception {
        System.out.println(credentials);
        String plainText = encryptionHelper.decrypt(credentials);
        System.out.println(plainText);
    }

    /*@GetMapping("/checksum")
    public Response<String> checksum(@RequestParam("token") String token, @RequestParam("price") Float price) {

    }*/
}
