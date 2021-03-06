#!/usr/bin/env bash
cur_dir=`pwd`
docker stop alpha-rpc-mysql
docker rm alpha-rpc-mysql
docker run --name alpha-rpc-mysql -v ${cur_dir}/conf:/etc/mysql/conf.d -v ${cur_dir}/data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=qwer1234 -d mysql:latest