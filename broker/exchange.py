#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.exchange_declare(exchange='logs',
                         exchange_type='fanout')

channel.basic_publish(exchange='logs',
                      routing_key='',
                      body=message)

result = channel.queue_declare(queue='', exclusive=True)

channel.queue_bind(exchange='logs',
                   queue=result.method.queue)