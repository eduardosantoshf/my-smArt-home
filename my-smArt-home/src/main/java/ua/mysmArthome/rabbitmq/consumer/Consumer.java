package ua.mysmArthome.rabbitmq.consumer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.Device;
import ua.mysmArthome.model.Notification;
import ua.mysmArthome.repository.DeviceRepository;
import ua.mysmArthome.repository.NotificationRepository;

public class Consumer {

    private static final String EXCHANGE = "logs";

    private Connection connection;
    private Channel channel;
    private HashMap<String, ArrayList<String>> notifications;

    public Consumer() throws ResourceNotFoundException {
        notifications = new HashMap<String,ArrayList<String>>();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        consumeQueue();
    }

    public void consumeQueue() throws ResourceNotFoundException{
        try {
            channel.exchangeDeclare(EXCHANGE, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String queueName="";
        try {
            queueName = channel.queueDeclare().getQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.queueBind(queueName, EXCHANGE, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JSONObject obj = new JSONObject(message);
            String not="";
            boolean harmful=false;
            double val;
            //{"property":{"name":"humidity","value":"35.547279816907306"},"id":"8997506"}
            System.out.println("tipo: "+ obj.getJSONObject("property").getString("name"));
            switch(obj.getJSONObject("property").getString("name")){

                case "humidity":
                    val = Double.parseDouble(obj.getJSONObject("property").getString("value"));
                    if(val<15 || val>50) {
                        not += "harmful " + obj.getJSONObject("property").getString("value");
                        harmful=true;
                    }else
                        not+="normal condition";

                    if (!notifications.containsKey(obj.getString("id")) )
                        notifications.put(obj.getString("id"), new ArrayList<>());

                    notifications.get(obj.getString("id")).add(not);
                    break;

                case "termal":
                    val = Double.parseDouble(obj.getJSONObject("property").getString("value"));
                    if(val<10 || val>35){
                        not+="harmful "+obj.getJSONObject("property").getString("value") + "º";
                        harmful=true;
                    }else
                        not+="normal condition";

                    if (!notifications.containsKey(obj.getString("id")) )
                        notifications.put(obj.getString("id"), new ArrayList<>());

                    notifications.get(obj.getString("id")).add(not);
                    break;

                case "proximity":
                    val = Double.parseDouble(obj.getJSONObject("property").getString("value"));
                    if(val<20){
                        not+="harmful "+obj.getJSONObject("property").getString("value")+" meters from sensor";
                        harmful=true;
                    }else
                        not+="normal condition";

                    if (!notifications.containsKey(obj.getString("id")) )
                        notifications.put(obj.getString("id"), new ArrayList<>());

                    notifications.get(obj.getString("id")).add(not);
                    break;

                case "alarm":
                    if(obj.getJSONObject("property").getString("value").equals("True")){
                        not+="harmful, alarm is ringing";
                        harmful=true;
                    }else
                        not+="normal condition";

                    if (!notifications.containsKey(obj.getString("id")) )
                        notifications.put(obj.getString("id"), new ArrayList<>());

                    notifications.get(obj.getString("id")).add(not);
                    break;

                case "door":
                    if(obj.getJSONObject("property").getString("value").equals("True")){
                        not+="harmful, door is ringing";
                        harmful=true;
                    }else
                        not+="normal condition";

                    if (!notifications.containsKey(obj.getString("id")) )
                        notifications.put(obj.getString("id"), new ArrayList<>());

                    notifications.get(obj.getString("id")).add(not);
                    break;

                default:break;
            }
            System.out.println(" [x] Received '" + message + "'");

        };//a callback in the form of an object that will buffer the messages until we're ready to use them

        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //{"status":"harmful | normal", "id":"id_do_device", "property":{"name":"humidity", "value":"80"}}
    }

    public HashMap<String, ArrayList<String>> getNotifications() {
        return notifications;
    }

    /*<String> getNotifications(String id){
        //get notifications of a certain device
        ArrayList<String> notificationsToSend = notifications.get(id);

        notifications.put(id, new ArrayList<String>());
        return notificationsToSend;
    }*/
}