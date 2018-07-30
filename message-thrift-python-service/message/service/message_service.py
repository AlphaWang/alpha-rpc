from message.service.generated import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocal import TBinaryProtocal
from thrift.server import TServer

class MessageServiceHandler:

    def sendMobileMessage(self, mobile, message):
        print "send mobile message"
        return True

    def sendEmailMessage(self, email, message):
        print "send email message"
        return True

if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)
    transport = TSocket.TServerSocket("localhost", "9090")
    tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocal.TBinaryProtocalFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print "==========python thrift server start."
    server.serve()
    print "==========python thrift server exit."
