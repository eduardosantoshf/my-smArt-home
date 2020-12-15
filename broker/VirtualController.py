import requests
import argparse
import socket
import json
import ast
import time
import random



class VirtualController():
    def __init__(self):
        self.localIP=(([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")] or [[(s.connect(("8.8.8.8", 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) + ["no IP found"])[0]
        self.devices={}
    def hardCheck(self):
        self.devices={}
        tipos=["sec_camera", "humidity", "termal", "proximity", "alarm", "light", "plug", "door"]
        ip_parts=self.localIP.split(".") # ip_parts = ['192', '168', '0', '100'], por exemplo
        for x in range(256):
            r=random.random()
            if r>0.99:
                device_ip=ip_parts[0]+"."+ip_parts[1]+"."+ip_parts[2]+"."+str(x)
                device_id=random.random()*9999999999999999
                random.shuffle(tipos)
                self.devices[str(device_id)]={"status":"turned-Off", "ip_":device_ip, "id_":device_id, "type_":tipos[0]}

        return self.devices # retorna uma dicionario cujas keys são [ip, ip, ip , ip, ...]
    def checkDevice(self, id):
        if ip in self.devices:
            return self.devices[id].type
        return None

    def turnOn(self, id):
        devices[id]={"status":"turned-On", "id_":devices[ip].id_, "type_":devices[ip].type_}
        #send data

    def turnOff(self, id):
        devices[id]={"status":"turned-Off", "id_":devices[ip].id_, "type_":devices[ip].type_}
        #send data

    def setBrightness(self, id, brightness):
        #send data
        if self.devices[id].type=="BULB":
            return brightness
        return False
    
    def getDevice(self, id):
        return [x for x in self.devices if x.id_==id][0]
    
    #properties
    def status(self, id):
        print(self.devices[id])
        return self.devices[id].status
    def humidity(self, id):
        if self.devices[id].type_=="humidity":
            device_humidity=random.random()*100
            return device_humidity
        return None
    def temperature(self, id):
        if self.devices[id].type_=="termal":
            device_temperature=random.random()*30
            return device_temperature
        return None
    def proximity(self, id):
        if self.devices[id].type_=="proximity":
            device_proximity=random.random()*10
            return device_proximity
        return None
    def ringing(self, id):
        if self.devices[id].type_=="door" or self.devices[id].type_=="alarm":
            isRinging=random.random()
            if isRinging>0.5:
                return True
            return False
        return None
    def brightness(self, id):
        if self.devices[id].type_=="light":
            device_brightness=random.random()*100
            return device_brightness
        return None
        
# tests
#
#vc=VirtualController()
#print(vc.hardCheck()) # pesquisa em toda a rede
#print(vc.checkDevice("192.168.0.106")) # verifica o tipo do dispositivo (BULB ou PLUG)
#vc.turnOn("192.168.0.106") # Liga o dispositivo
#vc.setBrightness("192.168.0.106", 1) # Se for um BULB, define o brilho
