package com.kshitij.cloudprint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshitij.cloudprint.ContentType;
import com.kshitij.cloudprint.configuration.CloudPrintProperties;
import com.kshitij.cloudprint.configuration.EncryptionHelper;
import com.kshitij.cloudprint.configuration.JwtTokenUtil;
import com.kshitij.cloudprint.model.LoginResponse;
import com.kshitij.cloudprint.model.User;
import com.kshitij.cloudprint.retrofit.AuthApi;
import com.kshitij.cloudprint.retrofit.SubmitOutput;
import com.kshitij.cloudprint.service.LoginService;
import com.mongodb.client.gridfs.GridFSBucket;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Autowired
    LoginService loginService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

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
    public ResponseEntity<LoginResponse> signup(@RequestParam("credentials") String credentials) throws Exception {
        System.out.println(credentials);
        String plainText = encryptionHelper.decrypt(credentials);
        System.out.println(plainText);
        User user = new ObjectMapper().readValue(plainText, User.class);
        LoginResponse response;
        if (loginService.userNameExist(user.getUsername())) {
            response = new LoginResponse("", "User already exist", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String encryptedPassword = encryptionHelper.encrypt(user.getPassword());
        user = loginService.create(new User(user.getUsername(), encryptedPassword));
        String token = jwtTokenUtil.generateToken(user);
        response = new LoginResponse(token, "Signup Successful!", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup2")
    public ResponseEntity<LoginResponse> signup2(@RequestParam("username") String username, @RequestParam("password") String password) {
        LoginResponse response;
        if (loginService.userNameExist(username)) {
            response = new LoginResponse("", "User already exist", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String encryptedPassword = encryptionHelper.encrypt(password);
        User user = loginService.create(new User(username, encryptedPassword));
        String token = jwtTokenUtil.generateToken(user);
        response = new LoginResponse(token, "Signup Successful!", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam("credentials") String credentials) throws Exception {
        System.out.println(credentials);
        String plainText = encryptionHelper.decrypt(credentials);
        System.out.println(plainText);
        User user = new ObjectMapper().readValue(plainText, User.class);
        String encryptedPassword = encryptionHelper.encrypt(user.getPassword());
        user = loginService.loginWith(user.getUsername(), encryptedPassword);
        LoginResponse response;
        if (user != null) {
            String token = jwtTokenUtil.generateToken(user);
            response = new LoginResponse(token, "Login Successful!", true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response = new LoginResponse("", "Wrong credentials", false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /*@GetMapping("/checksum")
    public Response<String> checksum(@RequestParam("token") String token, @RequestParam("price") Float price) {

    }*/
    @GetMapping("/checkToken")
    public Boolean checkToken(@RequestParam("token") String token) {
        if (jwtTokenUtil.validateToken(token))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
