# SMTP-Java-Mail-User-Agent

This is an implementation of a mail user agent that sends emails to remote hosts. The program uses the SMTP interaction between the MUA and the remote SMTP server. The client provides a graphical user interface containing fields for entering the sender and recipient addresses, the subject of the message and the message itself.

With this interface, when you want to send a mail, you must fill in complete addresses for both the sender and the recipient, i.e., user@someschool.edu, not just simply user. You can send mail to only one recipient. Furthermore, the domain part of the recipient's address must be the name of the SMTP server handling incoming mail at the recipient's site. For example, if you are sending mail to address user@someschool.edu and the SMTP server of someschool.edu is smtp.somechool.edu, you will have to use the address user@smtp.someschool.edu in the To-field. This is because Java doesn't support DNS lookups except for simple name-to-address queries. When you have finished composing your mail, press Send to send it. 


This project is built in Eclipse. To run simply import it into the IDE. The purpose of this project was to edit and fix the SMTPConnection.java class to provide a working implementation of the application having already been given the JFrame and message envelope.

SMTPConnection.java workflow logic:
- this class gets called and the SMTPConnection is created and a socket is set up;
- a stream is set up to read and write to the socket;
- connected to the default SMPT port 25;
- if our server responds OK the SMPT handshake is established and message is sent;
- the SMTP connection closes;
- the socket closes;

Added Commands and (Replies):
DATA(354), HELO (250), MAIL FROM (250), QUIT (221), RCPT TO (250) 
