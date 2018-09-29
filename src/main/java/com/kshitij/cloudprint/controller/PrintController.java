package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.ContentType;
import com.kshitij.cloudprint.retrofit.AuthApi;
import com.kshitij.cloudprint.retrofit.Intercepter;
import com.kshitij.cloudprint.retrofit.SubmitOutput;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@RestController
@RequestMapping("/print")
public class PrintController {
    /*@Autowired
    CloudPrintProperties properties;*/
    @Autowired
    FilesController filesController;
    @Autowired
    ContentType type;


    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void submitJob(@RequestParam("file") MultipartFile file) {
        AuthApi api = new Retrofit
                .Builder()
                .baseUrl("https://www.google.com/cloudprint/")
                .client(new OkHttpClient
                        .Builder()
                        .addInterceptor(new Intercepter("ya29.GlsnBtRioESEXerzyW1I6HP6VQxdo_vUTAUpSm5ulaP4j3CgCQn6GctOnOs9_kwqZXoc2QaQY1l6WfZb2ifmU1klwBIHlaLnFWsDChzuo-Gv0TLjaSLaXTkG0CAI")).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi.class);
        String name[] = file.getOriginalFilename().split("\\.");
        String ticket = "{\n" +
                "  \"version\": \"1.0\",\n" +
                "  \"print\": {\n" +
                "    \"vendor_ticket_item\": [],\n" +
                "    \"color\": {\"type\": \"STANDARD_MONOCHROME\"},\n" +
                "    \"copies\": {\"copies\": 1}\n" +
                "  }\n" +
                "}";
        try {
            Call<SubmitOutput> call = api.submitJob(
                    "8d31c85a-6681-43ef-1cb1-fdc6150b71dd",
                    name[0],
                    ticket,
                    file.getBytes(),
                    type.getContentType(name[1])
            );
            call.enqueue(new Callback<SubmitOutput>() {
                @Override
                public void onResponse(Call<SubmitOutput> call, Response<SubmitOutput> response) {
                    System.out.println(response.body());
                }

                @Override
                public void onFailure(Call<SubmitOutput> call, Throwable throwable) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
