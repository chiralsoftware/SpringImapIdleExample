# SpringImapIdleExample
Very simple project to show receiving emails using IMAP with Spring Integration
Configure the IMAP URL and run it and it will watch an inbox using IMAP. It works with IMAP idle so that
when a message comes in, that sends a message within Spring. This very simple example shows it 
looking for multipart messages that contains JPEGs. If it does, the JPEG will be parsed and the size
will be displayed. This idle IMAP reader will re-connect to the server if the connection is interrupted.
It's a great way to watch for incoming messages and shows how it can be done with minimal code. This
isn't well documented in the Spring Integration documentation.
