-- use mydb; 

-- insert into users(username, name, email, role, createdOn, updatedOn ) values('sanit', 'Sanit Sirisawatvatana', 'sanit.sir@kmutt.ac.th', 'admin', '2023-08-15 08:00:00+07:00', '2023-08-15 08:00:00+07:00');
-- insert into users(username, name, email, role, createdOn, updatedOn ) values('pornthip', 'Pornthip Sirijutikul', 'pornthip.sri@kmutt.ac.th', 'announcer', '2023-08-15 09:30:00+07:00', '2023-08-15 09:30:00+07:00');
-- insert into users(username, name, email, role, createdOn, updatedOn ) values('jaruwan_w', 'Jaruwan Maneesart', 'jaruwan.wee@kmutt.ac.th', 'announcer', '2023-08-16 08:00:00+07:00', '2023-08-16 08:00:00+07:00');
-- insert into users(username, name, email, role, createdOn, updatedOn ) values('vichchuda', 'Vichchuda Tedoloh', 'vichchuda.ted@kmutt.ac.th', 'announcer', '2023-08-16 09:30:00+07:00', '2023-08-16 09:30:00+07:00');

insert into users (username,password,name,email,role,createdOn,updatedOn) values
('sanit','$argon2id$v=19$m=4096,t=3,p=1$fabZIRZZrUuxRrnXoGkMjA$DZ90F9+3/rnHGKY/YmyQoZwOToS4mJbuLYZkpXQt2VY','Sanit Sirisawatvatana','sanit.sir@kmutt.ac.th','admin','2023-08-15 08:00:00+07:00','2023-08-15 08:00:00+07:00'),
('pornthip','$argon2id$v=19$m=4096,t=3,p=1$mZdjYXK6+s34Zd6PeVQG2Q$S9TGRhlIw2l7kIFYyPLJk6T5TDNHYa90Z/4gHubUNE8','Pornthip Sirijutikul','pornthip.sri@kmutt.ac.th','announcer','2023-08-15 09:30:00+07:00','2023-08-15 09:30:00+07:00'),
('jaruwan_w','$argon2id$v=19$m=4096,t=3,p=1$pTop1432FBQ8IlbizoaWsQ$6tG5y9QZ5uOz7qQe41vSwdr6wwnmLU9RFWrORpRaLdo','Jaruwan Maneesart','jaruwan.wee@kmutt.ac.th','announcer','2023-08-16 08:00:00+07:00','2023-08-16 08:00:00+07:00'),
('vichchuda','$argon2id$v=19$m=4096,t=3,p=1$XV/IqnCROw+/qCsQrD918g$bkcBkt4zmphxiVdYN0f2IvH8YW10jaHG9Sp7jmijycU','Vichchuda Tedoloh','vichchuda.ted@kmutt.ac.th','announcer','2023-08-16 09:30:00+07:00','2023-08-16 09:30:00+07:00');