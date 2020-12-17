package ua.mysmArthome.rabbitmq.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.*;

public class RpcProducer implements AutoCloseable{
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RpcProducer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory(); //connect to rabbitmq
        factory.setHost("localhost");

        connection = factory.newConnection(); //estabilish connection
        channel = connection.createChannel(); //estabilish channel
    }

    public static void main(String[] argv) {
        try (RpcProducer rpcclient = new RpcProducer()) {
            //message for the server (consumer)

                String response = rpcclient.call(createMessage());
                System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString(); //generate a unique correlationID

        //Then, we create a dedicated exclusive queue for the reply and subscribe to it.
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8")); //publish the message

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1); //will wait for one response

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        }); //consume response of the server (consumer)

        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
    
    private static String createMessage(){
        String response="";
        
        JSONObject jo = new JSONObject();

        jo.put("op","get");
        jo.put("property","ringing");
        jo.put("id","1159947561887127.0");
        
        response += jo.toString();
        return response;
    }
}
