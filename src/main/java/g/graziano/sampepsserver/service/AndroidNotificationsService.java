package g.graziano.sampepsserver.service;


import g.graziano.sampepsserver.HeaderRequestInterceptor;
import g.graziano.sampepsserver.model.data.Child;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import org.json.JSONObject;


@Service
public class AndroidNotificationsService {

    private static final Logger logger = LoggerFactory.getLogger(AndroidNotificationsService.class);

    @Value("${firebase.server.key}")
    private String FIREBASE_SERVER_KEY;

    @Value("${firebase.server.url}")
    private String FIREBASE_API_URL;

    private void send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

    }

    public void send(Child child) {

        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + child.getFamilyName());
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        String title = child.getName() + "'s status updated";
        notification.put("title", title);
        if(child.getSessions() != null && child.getSessions().iterator().hasNext()) {
            notification.put("body", "Last location: " + child.getSessions().iterator().next().getAddressString());
        }

        body.put("notification", notification);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        this.send(request);

    }
}