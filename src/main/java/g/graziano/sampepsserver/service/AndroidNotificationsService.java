package g.graziano.sampepsserver.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import g.graziano.sampepsserver.HeaderRequestInterceptor;
import g.graziano.sampepsserver.model.data.Child;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Service
public class AndroidNotificationsService {

    private static final Logger logger = LoggerFactory.getLogger(AndroidNotificationsService.class);

    @Value("${firebase.server.key}")
    private String FIREBASE_SERVER_KEY;

    @Value("${firebase.server.url}")
    private String FIREBASE_API_URL;



    @PostConstruct
    public void init(){
        FileInputStream serviceAccount = null;
        try {




            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream("google-services.json");



        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp firebaseApp =  FirebaseApp.initializeApp(options);
        FirebaseMessaging.getInstance(firebaseApp);

        // See documentation on defining a message payload.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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


    public void senddVWithSDK(Child child){

        String title = child.getName() + "'s status updated";
        String body = "Last location: " + child.getSessions().iterator().next().getAddressString();

        Message message = Message.builder()
                .setNotification(new Notification(title, body))
                .putData("time", "2:45")
                .setTopic(child.getFamilyName())
                .build();

// Send a message to the devices subscribed to the provided topic.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            logger.error(e.toString());
        }
// Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

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