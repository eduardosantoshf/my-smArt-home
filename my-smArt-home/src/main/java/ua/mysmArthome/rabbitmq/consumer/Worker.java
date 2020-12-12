package ua.mysmArthome.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {
    private final static String QUEUE_NAME = "hello"; //queue from which we are going to receive

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
    
        boolean durable = true; //To make sure the messages stay even if the server restarts
        channel.queueDeclare(QUEUE_NAME,durable, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        int prefetchCount = 1;
        channel.basicQos(prefetchCount); //This tells RabbitMQ not to give more than one message to a worker at a time (accepts only one unack-ed message at a time).

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } 
            catch (InterruptedException e) {
                    e.printStackTrace();
                } 
            finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            };//a callback in the form of an object that will buffer the messages until we're ready to use them

            boolean autoAck = false; //set this flag to false and send a proper acknowledgment from the worker, once we're done with a task

            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
            
        }

      private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
