package com.example.service;

import com.example.entity.SmsHistoryEntity;
import com.example.enums.ProfileStatus;
import com.example.repository.SmsHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
@Slf4j
@Service
public class SmsServerService {

    @Value("${my.eskiz.uz.email}")
    private String email;
    @Value("${my.eskiz.uz.password}")
    private String password;
    @Value("${sms.fly.uz.url}")
    private String url;
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;


    public void send(String phone, String text, String code) {
         saveHistory(phone, code,ProfileStatus.REGISTRATION);
        sendSmsHTTP(phone, text + code);
    }

    public void saveHistory(String phone, String code,ProfileStatus profileStatus) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setStatus(profileStatus);
        entity.setPhone(phone);
        entity.setMessage(code);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }


    public String getTokenWithAuthorization() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url + "/api/auth/login")
                .method("POST", body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.warn("connection error");
                throw new IOException();
            } else {
                JSONObject object = new JSONObject(response.body().string());
                JSONObject data = object.getJSONObject("data");
                Object token = data.get("token");
                return token.toString();
            }
        } catch (IOException e) {
            log.warn("error in token");
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    public void sendSmsHTTP(String phone, String text) {
        String token = "Bearer " + getTokenWithAuthorization();
        OkHttpClient client = new OkHttpClient();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone", phone)
                .addFormDataPart("message", text)
                .addFormDataPart("from", "4546")
                .build();

        Request request = new Request.Builder()
                .url(url + "/api/message/sms/send")
                .method("POST", body)
                .header("Authorization", token)
                .build();
//        try {
//            Response response = client.newCall(request).execute();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Thread thread = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                log.warn("connection error");
                e.printStackTrace();
            }
        });
        thread.start();
    }


}