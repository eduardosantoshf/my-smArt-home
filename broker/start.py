from PyP100 import PyP100
import argparse
import socket

class TapoController():
    def __init__(self):
        self.localIP=(([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")] or [[(s.connect(("8.8.8.8", 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) + ["no IP found"])[0]
        self.email='iesproject2020@gmail.com'
        self.password='666diablofodido'
        self.devices=[]
    def hardCheck(self):
        ip_parts=self.localIP.split(".") # ip_parts = ['192', '168', '0', '100'], por exemplo
        for x in range(256):
            ip=ip_parts[0]+"."+ip_parts[1]+"."+ip_parts[2]+"."+str(x) # extraimos o prefixo da rede: 192.168.0.xxx
            print(ip)
            p100=PyP100.P100(ip, "iesproject2020@gmail.com", "666diablofodido")
            try:
                p100.handshake()
                self.devices.append(ip)
            except:
                continue
        print(self.devices)
    def checkDevice(self, ip):
        # verificar se o dispositivo é uma lampada ou uma tomada
        # return "LAMP" para lampada
        # return "PLUG" para tomada
        pass

tc=TapoController()
tc.hardCheck()

exit()
p100.handshake()
p100.login()


parser=argparse.ArgumentParser(description='Process operation')
parser.add_argument('-op', required=True, help='Choose turn-on or turn-off')
args=vars(parser.parse_args())

if args["op"]=='turn-on':
    p100.turnOn()
elif args["op"]=='turn-off':
    p100.turnOff()
else:
    exit(0)

