create database ies;
create user 'demo'@'%' identified by 'root';
grant all on ies.* to 'demo'@'%';
flush privileges;
