import requests
from PyP100 import PyP100
import argparse
import socket
import json
import ast
import time

class TapoController():
    def __init__(self):
        self.localIP=(([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")] or [[(s.connect(("8.8.8.8", 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) + ["no IP found"])[0]
        self.email='iesproject2020@gmail.com'
        self.password='666diablofodido'
        self.devices=[]
    def hardCheck(self):
        self.devices=[]
        ip_parts=self.localIP.split(".") # ip_parts = ['192', '168', '0', '100'], por exemplo
        for x in range(256):
            ip=ip_parts[0]+"."+ip_parts[1]+"."+ip_parts[2]+"."+str(x) # extraimos o prefixo da rede: 192.168.0.xxx
            print(ip)
            p100=PyP100.P100(ip, self.email, self.password)
            try:
                p100.handshake()
                self.devices.append(ip)
            except:
                continue
        return self.devices # retorna uma lista [ip, ip, ip , ip, ...]
    def checkDevice(self, ip):
        p100=PyP100.P100(ip, self.email, self.password)
        p100.handshake()
        p100.login()
        obj=json.loads(p100.getDeviceInfo())
        if obj["result"]["type"]=="SMART.TAPOPLUG":
            return "PLUG"
        elif obj["result"]["type"]=="SMART.TAPOBULB":
            return "BULB"
        return None
        # verificar se o dispositivo é uma lampada ou uma tomada
        # return "BULB" para lampada
        # return "PLUG" para tomada
        pass

    def turnOn(self, ip):
        p100=PyP100.P100(ip, self.email, self.password)
        p100.handshake()
        p100.login()
        p100.turnOn()

    def turnOff(self, ip):
        p100=PyP100.P100(ip, self.email, self.password)
        p100.handshake()
        p100.login()
        p100.turnOff()

    def setBrightness(self, ip, brightness):
        if self.checkDevice(ip)!="BULB":
            return None
        
        p100=PyP100.P100(ip, self.email, self.password)
        p100.handshake()
        p100.login()
        token=p100.token
        cookie=p100.cookie

        URL = f"http://{ip}/app?token={token}"
        Payload = { "method": "set_device_info", "params":{ "brightness": brightness }, "requestTimeMils": int(round(time.time() * 1000)) }

        headers = {
        "Cookie": cookie
        }

        EncryptedPayload = p100.tpLinkCipher.encrypt(json.dumps(Payload))

        SecurePassthroughPayload = {
            "method": "securePassthrough",
            "params":{
                "request": EncryptedPayload
            }
        }

        r = requests.post(URL, json=SecurePassthroughPayload, headers=headers)

        decryptedResponse = p100.tpLinkCipher.decrypt(r.json()["result"]["response"])

        if ast.literal_eval(decryptedResponse)["error_code"] != 0:
            errorCode = ast.literal_eval(decryptedResponse)["error_code"]
            errorMessage = p100.errorCodes[str(errorCode)]

# tests
#
#tc=TapoController()
#tc.hardCheck() # pesquisa em toda a rede
#tc.checkDevice("192.168.0.106") # verifica o tipo do dispositivo (BULB ou PLUG)
#tc.turnOn("192.168.0.106") # Liga o dispositivo
#tc.setBrightness("192.168.0.106", 1) # Se for um BULB, define o brilho