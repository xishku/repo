# 0 * * * * cd /home/g/xxx/mail/; python3 smtp_mail.py


import random
import time
from datetime import datetime
import smtplib
from email.mime.text import MIMEText

class SmtpMailAgent:
    def __init__(self, mail_host, mail_user, mail_pass) -> None:
        self._host = mail_host
        self._user = mail_user
        self._pass = mail_pass

    @staticmethod
    def join_recv_names(recv_lst):
        names = ""
        for name in recv_lst:
            names = names + name + ";"
        
        return names

    def send(self, sender, receivers, title, content) -> None:
        #message = MIMEText(msg_string, 'plain', 'utf-8')#发送纯文本邮件
        message = MIMEText(msg_html, 'html', 'utf-8')#发送html格式邮件

        message['Subject'] = msg_string #邮件标题
        message['From'] = sender
        message['To'] = SmtpMailAgent.join_recv_names(receivers)

        try:
            smtpObj = smtplib.SMTP()
            smtpObj.connect(mail_host, 25)
            smtpObj.login(mail_user, mail_pass)
            smtpObj.sendmail(sender, receivers, message.as_string())
            smtpObj.quit()
            print('send success')
        except smtplib.SMTPException as e:
            print('send error', e)

if __name__== "__main__" :
    random_num = random.random() * 1000
    print(random_num, datetime.now())
    # time.sleep(random_num)
    print(random_num, datetime.now())

    mail_host = 'smtp.10086.cn'
    mail_user = '用户名'#
    mail_pass = '密码'#
    sender = '发件人'#自己邮箱地址
    receivers = ['收件人1', '收件人2']#d如果多个收件人“,”分开

    msg_string = '邮件标题'
    msg_html= """
        <html xmlns:v="urn:schemas-microsoft-com:vml"
        xmlns:o="urn:schemas-microsoft-com:office:office"
        xmlns:w="urn:schemas-microsoft-com:office:word"
        xmlns:m="http://schemas.microsoft.com/office/2004/12/omml"
        xmlns="http://www.w3.org/TR/REC-html40">

        <head>


        <p class=MsoNormal align=left style='text-align:left'><span style='font-family:
        宋体;color:#1F497D'>大家好，</span><span lang=EN-US style='color:#1F497D'><o:p></o:p></span></p>

        """
    
    agent = SmtpMailAgent(mail_host, mail_user, mail_pass)
    agent.send(sender, receivers, title=msg_string, content=msg_html)


