package com.alphawang.rpc.user.thrift;

import com.alphawang.rpc.thrift.user.service.api.UserService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class UserThriftServer {
    
    @Value("${service.port}")
    private int servicePort;
    
    @Autowired
    private UserService.Iface userService;
    
    @PostConstruct
    public void startThriftServer() {
        TProcessor processor = new UserService.Processor<>(userService);
        TNonblockingServerSocket serverSocket = null;
        try {
            serverSocket = new TNonblockingServerSocket(servicePort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }

        TNonblockingServer.Args args = new TNonblockingServer.Args(serverSocket);
        args.processor(processor);
        args.transportFactory(new TFramedTransport.Factory());
        args.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TNonblockingServer(args);
        server.serve();
    }
}
