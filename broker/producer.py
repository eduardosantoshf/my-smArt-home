import pika
import sys
import random
import time
import json

class Generator:
    def __init__(self, devices):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host = 'rabbitmq'))
        self.channel = self.connection.channel()
        self.channel.exchange_declare(exchange = 'logs', exchange_type = 'fanout')
        self.devices=devices

    def randomInfo(self):
        # escolher aleatoriamente um tipo de device
        f=open("db_devices.txt", "rb")
        content=f.read()
        content=json.loads(content.decode('latin'))
        devices=content["devices"]
        
        devices=[d for d in devices if d["status"]=="turned-On"]
        print("Devices:")
       
        if len(devices)>0:
            random.shuffle(devices)
            
            device=devices[0]
            retorno=""
            if device["type"]=="humidity" or device["type"]=="termal" or device["type"]=="proximity":
                retorno=str(random.random()*100)
            if device["type"]=="alarm" or device["type"]=="door":
                r=random.random()
                if r<0.50:
                    retorno="True"
                else:
                    retorno="False"
            self.channel.basic_publish(exchange = 'logs', routing_key = '', body = '{"id":"'+device["id"]+'", "property":{"name":"'+device["type"]+'", "value":"'+retorno+'"}}')
        else:
            self.channel.basic_publish(exchange = 'logs', routing_key = '', body = '{"id":"-1", "property":{"name":"none", "value":"none"}}')

if __name__ == "__main__":
    f=open("db_devices.txt", "rb")
    content=f.read()
    content=json.loads(content.decode('latin'))

    g = Generator(content["devices"])

    while True:
        g.randomInfo()
        time.sleep(3)
