FROM python:3.8
WORKDIR /usr/src/app
COPY . .
RUN python3 -m pip install PyP100
RUN pip install pika
RUN pip install requests

ADD wait-for-it.sh /
RUN chmod u+x ./wait-for-it.sh

CMD ["./wait-for-it.sh","rabbitmq:5672","--","python3", "rpc_server.py"]
CMD ["./wait-for-it.sh","rabbitmq:5672","--","python3", "producer.py"]