import pika
import sys
import random
import time

class Generator:

    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host = 'localhost'))
        self.channel = self.connection.channel()
        self.channel.exchange_declare(exchange = 'logs', exchange_type = 'fanout')

    def fridge_temperature_generator(self):
        r = random.random() * 10
        if r > 9:
            time.sleep(2)
            self.channel.basic_publish(exchange = 'logs', routing_key = '', body = "Fridge temperature: " + str(r))

    def hoven_temperature_generator(self, hoven_temp):
        n = 0
        while n < hoven_temp:
            time.sleep(2)
            self.channel.basic_publish(exchange = 'logs', routing_key = '', body = "Hoven temperature: " + str(n))
            n += 1

if __name__ == "__main__":
    
    g = Generator()

    while True:
        g.fridge_temperature_generator()
        g.hoven_temperature_generator(180)