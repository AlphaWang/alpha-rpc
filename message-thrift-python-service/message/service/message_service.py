#coding: utf-8
from message.service.generated import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocal import TBinaryProtocal
from thrift.server import TServer

import smtplib
from email.mime.text import MIMEText
from email.header import Header

sender = '1919wang@163.com'
authCode = 'qwer1234'
class MessageServiceHandler:

    def sendMobileMessage(self, mobile, message):
        print "sending mobile message, mobile: " + mobile + ", msg: " + message
        return True

    def sendEmailMessage(self, email, message):
        print "sending email message, email: " + email +", msg: " + message
        messageObj = MIMEText(message, "plain", "utf-8")
        messageObj['From'] = sender
        messageObj['To'] = email
        messageObj['Subject'] = Header('Message From Alpha-RPC', 'utf-8')
        try:
            smtpObj = smtplib.SMTP('smpt.163.com')
            smtpObj.login(sender, authCode)
            smtpObj.sendmail(sender, [email], messageObj.as_string())
            print 'send mail success.'
            return True
        except smtplib.SMTPException, ex:
            print 'send mail failed.'
            print ex
            return False
        

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
