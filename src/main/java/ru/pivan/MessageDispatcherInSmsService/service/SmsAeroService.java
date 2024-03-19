package ru.pivan.MessageDispatcherInSmsService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class SmsAeroService {

    private final String email;
    private final String apiKey;
    private final String url;
    private final String send;
    private final String status;
    private final String username;

    @Autowired
    public SmsAeroService(@Value("${sms.sms_aero.email}") String email,
                          @Value("${sms.sms_aero.api_key}") String apiKey,
                          @Value("${sms.sms_aero.url}") String url,
                          @Value("${sms.sms_aero.url.send}") String send,
                          @Value("${sms.sms_aero.url.status}") String status,
                          @Value("${sms.sms_aero.username}") String username) {
        this.email = email;
        this.apiKey = apiKey;
        this.url = url;
        this.send = send;
        this.status = status;
        this.username = username;
    }

    public String notifyUser(String message, String phoneNumber){

        // добавление заголовков аутентификации
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(email, apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        // создание url со всеми параметрами
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + send)
                .queryParam("sign", username)
                .queryParam("text", message)
                .queryParam("number", phoneNumber);

        try {
            ResponseEntity<String> req = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, request, String.class); // в случае не 200 вызывает исключение
            // получаю req, если bad request, то проверяю в чем причина. Если дело в номере, то удаляю его из списка и пересоздаю url
            return "Messages started to be sent";

        } catch (HttpClientErrorException e) {
            if(e.getStatusText().equals("402")){
                return "";
            }
            return e.getMessage();
        } catch (HttpServerErrorException e) {
            return "Server error: " + e.getStatusText() + " bad request";
        } catch (Exception e){
            return "Unexpected error";
        }
    }
}
