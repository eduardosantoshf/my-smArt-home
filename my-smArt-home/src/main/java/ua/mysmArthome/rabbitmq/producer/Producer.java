package ua.mysmArthome.rabbitmq.producer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Producer {
    private final static String EXCHANGE = "logs";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory(); 
        //if we wanted to connect to a node on a different machine just specify its hostname and IP
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE, "fanout"); //broadcasts all the messages it receives to all the queues it knows. 

                String message = argv.length < 1 ? "info: Hello World!" : String.join(" ", argv);

                channel.basicPublish( "logs", "", null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
