import requests
import argparse
import socket
import json
import ast
import time
import random
from datetime import date

class VirtualController():
    def __init__(self):
        self.localIP="102.168.0."+str(round(random.random()*256))
        
        self.devices={}
    def hardCheck(self):
        self.devices={}
        tipos=["sec_camera", "humidity", "termal", "proximity", "alarm", "light", "plug", "door"]
        ip_parts=self.localIP.split(".") # ip_parts = ['192', '168', '0', '100'], por exemplo
        for x in range(256):
            r=random.random()
            if r>0.99:
                device_ip=ip_parts[0]+"."+ip_parts[1]+"."+ip_parts[2]+"."+str(x)
                device_id=round(random.random()*9999999)
                random.shuffle(tipos)
                self.devices[str(device_id)]={"status":"turned-Off", "ip_":device_ip, "id_":device_id, "type_":tipos[0], "active":date.today()}
        
        self.saveFile()
        return self.devices # retorna uma dicionario cujas keys são [ip, ip, ip , ip, ...]
    def checkDevice(self, id):
        if ip in self.devices:
            return self.devices[id].type_
        return None

    def turnOn(self, id):
        if id not in self.devices:
            return None
        self.devices[id]["status"]="turned-On"
        self.saveFile()
        return True

    def turnOff(self, id):
        if id not in self.devices:
            return None
        self.devices[id]["status"]="turned-Off"
        self.saveFile()
        return True

    def setBrightness(self, id, brightness):
        #send data
        if self.devices[id].type=="BULB":
            return brightness
        return False
    
    def getDevice(self, id):
        return [x for x in self.devices if x.id_==id][0]
    
    #properties
    def status(self, id):
        if id not in self.devices:
            return None
        return self.devices[id]["status"]
    def humidity(self, id):
        if id not in self.devices:
            return None
        if self.devices[id]["type_"]=="humidity":
            device_humidity=random.random()*100
            return device_humidity
        return None
    def temperature(self, id):
        if id not in self.devices:
            return None
        if self.devices[id]["type_"]=="termal":
            device_temperature=random.random()*30
            return device_temperature
        return None
    def proximity(self, id):
        if id not in self.devices:
            return None
        if self.devices[id]["type_"]=="proximity":
            device_proximity=random.random()*10
            return device_proximity
        return None
    def ringing(self, id):
        if id not in self.devices:
            return None
        if self.devices[id]["type_"]=="door" or self.devices[id]["type_"]=="alarm":
            isRinging=random.random()
            if isRinging>0.5:
                return True
            return False
        return None
    def brightness(self, id):
        if id not in self.devices:
            return None
        if self.devices[id]["type_"]=="light":
            device_brightness=random.random()*100
            return device_brightness
        return None

    def tipo(self, id):
        if id not in self.devices:
            return None
        return self.devices[id]["type_"]

    def active(self, id):
        if id not in self.devices:
            return None
        return self.devices[id]["active"]
    
    def saveFile(self):
        retorno='['
        counter=0
        for device_id in self.devices:
            counter+=1
            device=self.devices[device_id]
            retorno+='{"id":"'+str(device["id_"])+'", "type":"'+device["type_"]+'", "active":"'+str(device["active"])+'", "status":"'+self.status(str(device["id_"]))+'"}'
            if counter<len(self.devices.keys()):
                retorno+=','
        retorno+=']'
        retorno='{"devices":'+retorno+'}'

        f=open("db_devices.txt", "wb")
        f.write(retorno.encode('latin'))
        f.close()
        
# tests
#
#vc=VirtualController()
#print(vc.hardCheck()) # pesquisa em toda a rede
#print(vc.checkDevice("192.168.0.106")) # verifica o tipo do dispositivo (BULB ou PLUG)
#vc.turnOn("192.168.0.106") # Liga o dispositivo
#vc.setBrightness("192.168.0.106", 1) # Se for um BULB, define o brilho
