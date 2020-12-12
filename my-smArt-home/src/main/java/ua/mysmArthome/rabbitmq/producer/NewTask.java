package ua.mysmArthome.rabbitmq.producer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory(); 
        //if we wanted to connect to a node on a different machine just specify its hostname and IP
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
                boolean durable = true; //To make sure the messages stay even if the server restarts
                channel.queueDeclare(QUEUE_NAME,durable, false, false, null);
                
                String message = "alo....";

                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
                //Marking messages as persistent doesn't fully guarantee that a message won't be lost. 
                //Although it tells RabbitMQ to save the message to disk, there is still a short time window when RabbitMQ has accepted a message and hasn't saved it yet. 
        }
    }
}
