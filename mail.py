import smtplib # module to send mail

sender ="shirshadatta2000@gmail.com" #gives your or sender mail

receiver="malikdaksh115@gmail.com" #gives receiver or authority mail

password= "*********" #gives password of sender mail

message= "some error is found..." #message you want to send

server=smtplib.SMTP("smtp.gmail.com",587) #use to congiure mail server

server.starttls() # to start server

server.login(sender,password) #login as sender

server.sendmail(sender,receiver,message) #send mail from sender to receiver
print("message sent successfully")
