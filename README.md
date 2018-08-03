# alpha-rpc

## Arch Design
- Message Service (9090): python + thrift

![arch](arch.png)


## Prerequisites

### install thrift
https://www.imooc.com/article/30296

1. Download thrift [thrift.apache.org](http://thrift.apache.org)
2. Config
```
./configure --with-qt4=no  --with-qt5=no  --with-c_glib=no  --with-csharp=no  --with-erlang=no  --with-nodejs=no  --with-lua=no  --with-perl=no  --with-php=no  --with-php_extension=no  --with-dart=no  --with-ruby=no  --with-haskell=no  --with-go=no  --with-haxe=no  --with-d=no
```
3. Make
```
make && make install
```


### run mysql docker
```
cd mysql
sh run-mysql.sh
```

### db tables:
`db_user.pe_user`   
- id int
- username varchar
- password varchar
- real_name varchar
- mobile varchar
- email varchar

### run redis docker
```
cd redis
sh run-redis.sh
```

test connection:
```
netstat -na | grep 6379

telnet localhost 6379
set a b
get a
```