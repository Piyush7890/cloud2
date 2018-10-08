package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.ContentType;
import com.kshitij.cloudprint.configuration.CloudPrintProperties;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequestMapping("/print")
public class PrintController {
    @Autowired
    CloudPrintProperties properties;
    @Autowired
    FilesController filesController;
    @Autowired
    ContentType type;
    @Autowired
    AuthApi api;
    @Autowired
    GridFSBucket bucket;

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity submit(@RequestParam("file") MultipartFile file) throws IOException {
        /*AuthApi api = new Retrofit
                .Builder()
                .baseUrl(properties.getBaseUrl())
                .client(new OkHttpClient
                        .Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60,TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new Intercepter(properties.getToken())).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi.class);*/
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
        /*ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        InputStreamBody inputStreamBody = new InputStreamBody(byteArrayInputStream, org.apache.http.entity.ContentType.create("application/pdf"),name[0]);*/
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
}
