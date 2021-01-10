#!/usr/bin/env python
import pika
import json
from VirtualController import VirtualController

class Generator:
    def __init__(self):
        self.connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='rpc_queue')
        self.VirtualDevices = VirtualController()

    def on_request(self, ch, method, props, body):
        response="Null"
        command=json.loads(body)
        if command["op"]=="get": # get operation means you want to know something about the device, so, you need to send the "id" of the device you want to check, and the "property" you want to know
            device = command["id"]
            property=command["property"]
            value=None
            # Properties:
            # - status : all devices, returns 'turned-on' or 'turned-off'
            # - humidity : humidity sensor, returns a float
            # - temperature : termals sensor, returns a float
            # - proximity : proximity sensor, returns a float in meters
            # - ringing : door bell and alarm sensors, returns true or false
            # - brightness : lights, returns percentage between 1-100
            if property=="status":
                value=self.VirtualDevices.status(device)
            elif property=="humidity":
                value=self.VirtualDevices.humidity(device)
            elif property=="temperature":
                value=self.VirtualDevices.temperature(device)
            elif property=="proximity":
                value=self.VirtualDevices.proximity(device)
            elif property=="ringing":
                value=self.VirtualDevices.ringing(device)
            elif property=="brightness":
                value=self.VirtualDevices.brightness(device)
            elif property=="type":
                value=self.VirtualDevices.tipo(device)
            elif property=="active_since":
                value=self.VirtualDevices.active(device)
            
            if value==None:
                response='{"result":False, "reason":"Device not found"}'
            response='{"result":true, "'+property+'":"'+str(value)+'"}'
        elif command["op"]=="hardcheck": # hardcheck operation means you want to know all devices connected to your network, no extra information is required to be sent
            home_id = command["home_id"]
            devices=self.VirtualDevices.hardCheck()
            response='{"devices":['
            counter=0
            for d in devices:
                x=devices[d]
                counter+=1
                response+='{"status":"'+x["status"]+'", "ip_":"'+x["ip_"]+'", "id_":"'+str(x["id_"])+'", "type_":"'+x["type_"]+'"}'
                if counter<len(devices):
                    response+=","
            response+=']}'
        elif command["op"]=="turnOn": # turn on an especific device, so it need to specify the id
            ip=command["id"]
            retorno=self.VirtualDevices.turnOn(ip)
            response='{"result":true, "status":"turned-on"}'
            
            f=open('db_devices.txt','rb')
            file=f.read()
            devices=json.loads(file)
            devices=devices["devices"]
            print(devices)
            
        elif command["op"]=="turnOff": # turn off an especific device, so it need to specify the id
            ip=command["id"]
            retorno=self.VirtualDevices.turnOff(ip)
            response='{"result":true, "status":"turned-off"}'
        elif command["op"]=="brightness": # set (different) brightness to lights. only works on lighs and requires "id" and "brightness"
            ip=command["id"]
            brightness=command["brightness"]
            response='{"result":true, "brightness":"'+brightness+'"}'
        
        ch.basic_publish(exchange='',
                         routing_key=props.reply_to,
                         properties=pika.BasicProperties(correlation_id = \
                                                             props.correlation_id),
                         body=str(response))
        ch.basic_ack(delivery_tag=method.delivery_tag)


if __name__=="__main__":
    g=Generator()
    g.channel.basic_qos(prefetch_count=1)
    g.channel.basic_consume(queue='rpc_queue', on_message_callback=g.on_request)

    print(" [x] Awaiting RPC requests")
    g.channel.start_consuming()
