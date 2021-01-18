FROM python:3.8
WORKDIR /usr/src/app
COPY . .
RUN pip3 install -r requirements.txt
RUN pip3 install pika

ADD wait-for-it.sh /
RUN chmod u+x ./wait-for-it.sh

CMD ["./wait-for-it.sh", "rabbitmq:5672", "--", "python3", "rpc_server.py", ";", "python3","producer.py"]