package ua.mysmArthome.rabbitmq.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.*;

public class RpcProducer implements AutoCloseable {
    private String property = "";
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RpcProducer() {
        ConnectionFactory factory = new ConnectionFactory(); // connect to rabbitmq
        factory.setHost("localhost");

        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } // estabilish connection
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } // estabilish channel
    }

    public String createMessage(String option, String id) {

        final String corrId = UUID.randomUUID().toString(); // generate a unique correlationID

        // Then, we create a dedicated exclusive queue for the reply and subscribe to
        // it.
        String replyQueueName = "";
        try {
            replyQueueName = channel.queueDeclare().getQueue();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
                .build();

        String message = "";
        JSONObject jo = new JSONObject();
        jo.put("op", option);

        switch (option) {
            case "hardcheck": // used to get all available devices from the server
                break;
            case "get":
                jo.put("property", this.property);
                jo.put("id", id);
                break;
            case "turnOn":
                jo.put("id", id);
                break;
            case "turnOff":
                jo.put("id", id);
                break;
            case "brightness":
                jo.put("id", id);
                Random r = new Random();
                float brightness = r.nextFloat() * 90; // not using 100 so it doesnt reach 100%
                jo.put("brightness", brightness);
                break;
        }

        message += jo.toString();

        try {
            channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } // publish the message

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1); // will wait for one response

        String ctag="";
        try {
            ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.offer(new String(delivery.getBody(), "UTF-8"));
                }
            }, consumerTag -> {
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } // consume response of the server (consumer)

        String result="";
        try {
            result = response.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            channel.basicCancel(ctag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(" [.] Got '" + response + "'");
        
        return result;
    }
    
    public String createWithProperty(String option,String id,String property){
        this.property=property;
        return createMessage(option,id);
    }

    public void close() throws IOException {
        connection.close();
    }
}
