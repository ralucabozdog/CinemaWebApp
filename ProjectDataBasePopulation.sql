drop schema cinema;
create schema cinema;

#create tables

CREATE TABLE `cinema`.`customer` (
  `idcustomer` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `usertype` INT NOT NULL,
  PRIMARY KEY (`idcustomer`));

CREATE TABLE `cinema`.`movie` (
  `idmovie` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `poster` VARCHAR(200) NOT NULL,
  `rating` FLOAT NOT NULL,
  `descript` VARCHAR(700) NOT NULL,
  `cast` VARCHAR(300) NOT NULL,
  `director` VARCHAR(70) NOT NULL,
  `writer` VARCHAR(70) NOT NULL,
  `genre` VARCHAR(100) NOT NULL,
  `trailerurl` VARCHAR(200) NOT NULL,
  `duration` VARCHAR(20) NOT NULL,
  `classification` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`idmovie`));

CREATE TABLE `cinema`.`ticket` (
  `idticket` INT NOT NULL AUTO_INCREMENT,
  `customer` INT NOT NULL,
  `representation` INT NOT NULL,
  `seatrow` INT NOT NULL,
  `seatcol` INT NOT NULL,
  `price` FLOAT NOT NULL,
  PRIMARY KEY (`idticket`));
  
  CREATE TABLE `cinema`.`price`(
  `idprice` INT NOT NULL AUTO_INCREMENT,
  `amount` FLOAT NOT NULL,
  `typeofplay` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`idprice`));

CREATE TABLE `cinema`.`booking` (
  `idbooking` INT NOT NULL AUTO_INCREMENT,
  `customer` INT NOT NULL,
  `representation` INT NOT NULL,
  `seatrow` INT NOT NULL,
  `seatcol` INT NOT NULL,
  PRIMARY KEY (`idbooking`));

CREATE TABLE `cinema`.`representation` (
  `idrepresentation` INT NOT NULL AUTO_INCREMENT,
  `movie` INT NOT NULL,
  `dayofweek` INT NOT NULL,
  `starttime` VARCHAR(20) NOT NULL,
  `screen` INT NOT NULL,
  `typeofplay` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`idrepresentation`));
  
  CREATE TABLE `cinema`.`theatre` (
  `idtheatre` INT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idtheatre`));
  
  CREATE TABLE `cinema`.`screen` (
  `idscreen` INT NOT NULL AUTO_INCREMENT,
  `screenname` VARCHAR(20) NOT NULL,
  `theatre` INT NOT NULL,
  `nbrows` INT NOT NULL,
  `nbchairsrow` INT NOT NULL,
  PRIMARY KEY (`idscreen`));

#create foreign keys

alter table cinema.ticket
add constraint ticket_customer
foreign key (customer)
references cinema.customer (idcustomer);

alter table cinema.ticket
add constraint ticket_representation
foreign key (representation)
references cinema.representation (idrepresentation);

alter table cinema.booking
add constraint booking_customer
foreign key (customer)
references cinema.customer (idcustomer);

alter table cinema.booking
add constraint booking_representation
foreign key (representation)
references cinema.representation (idrepresentation);

alter table cinema.representation
add constraint representation_movie
foreign key (movie)
references cinema.movie (idmovie);

alter table cinema.representation
add constraint representation_screen
foreign key (screen)
references cinema.screen (idscreen);

alter table cinema.screen
add constraint screen_theatre
foreign key (theatre)
references cinema.theatre (idtheatre);





#populate database

use cinema;

#movie table

insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'Gladiator', 
						'https://m.media-amazon.com/images/M/MV5BMDliMmNhNDEtODUyOS00MjNlLTgxODEtN2U3NzIxMGVkZTA1L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg',
                        8.5, 
                        'A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.',
                        'Russel Crowe, Joaquin Phoenix, Connie Nielsen',
                        'Ridley Scott',
                        'David Franzoni, John Logan, William Nicholson',
                        'Action, Adevnture, Drama',
                        'https://www.youtube.com/embed/P5ieIbInFpg',
                        '2h 35m',
                        'R');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'Inception', 
                        'https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg',
                        8.8, 
						'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.',
                        'Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page',
                        'Christopher Nolan',
                        'Christopher Nolan',
                        'Action, Adventure, Sci-Fi, Thriller',
                        'https://www.youtube.com/embed/YoHD9XEInc0',
                        '2h 28m',
                        'PG-13');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'Interstellar', 
                        'https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg',
                        8.6, 
						"A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                        'Matthew McConaughey, Anne Hathaway, Jessica Chastain',
                        'Christopher Nolan',
                        'Jonathan Nolan, Christopher Nolan',
                        'Adventure, Drama, Sci-Fi',
                        'https://www.youtube.com/embed/2LqzF5WauAw',
                        '2h 49m',
                        'PG-13');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'Shutter Island', 
                        'https://m.media-amazon.com/images/M/MV5BYzhiNDkyNzktNTZmYS00ZTBkLTk2MDAtM2U0YjU1MzgxZjgzXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg',
                        8.2, 
						"In 1954, a U.S. Marshal investigates the disappearance of a murderer who escaped from a hospital for the criminally insane.",
                        'Leonardo DiCaprio, Emily Mortimer, Mark Ruffalo, Ben Kingsley',
                        'Martin Scorsese',
                        'Laeta Kalogridis, Dennis Lehane',
                        'Mystery, Thriller',
                        'https://www.youtube.com/embed/5iaYLCiq5RM',
                        '2h 18m',
                        'PG-13');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'The Matrix', 
                        'https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg',
                        8.7, 
						"When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
                        'Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving',
                        'Lana Wachowski, Lilly Wachowski',
                        'Lana Wachowski, Lilly Wachowski',
                        'Action, Sci-Fi',
                        'https://www.youtube.com/embed/vKQi3bBA1y8',
                        '2h 16m',
                        'R');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'The Prestige', 
                        'https://m.media-amazon.com/images/M/MV5BMjA4NDI0MTIxNF5BMl5BanBnXkFtZTYwNTM0MzY2._V1_.jpg',
                        8.5, 
						"After a tragic accident, two stage magicians in 1890s London engage in a battle to create the ultimate illusion while sacrificing everything they have to outwit each other.",
                        'Christian Bale, Hugh Jackman, Scarlett Johansson, Michael Caine, David Bowie',
                        'Christopher Nolan',
                        'Jonathan Nolan, Christopher Nolan, Christopher Priest',
                        'Drama, Mystery, Thriller',
                        'https://www.youtube.com/embed/ObGVA1WOqyE',
                        '2h 10m',
                        'PG-13');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'The Shawshank Redemption', 
                        'https://upload.wikimedia.org/wikipedia/ro/c/cb/The_Shawshank_Redemption.jpg',
                        9.3, 
						"Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        'Tim Robbins, Morgan Freeman, Bob Gunton',
                        'Frank Darabont',
                        'Stephen King, Frank Darabont',
                        'Drama',
                        'https://www.youtube.com/embed/6hB3S9bIaco',
                        '2h 22m',
                        'R');
                        
insert into movie (title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(
						'The Silence of the Lambs', 
                        'https://m.media-amazon.com/images/M/MV5BNjNhZTk0ZmEtNjJhMi00YzFlLWE1MmEtYzM1M2ZmMGMwMTU4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg',
                        8.6, 
						"A young F.B.I. cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer, a madman who skins his victims.",
                        'Jodie Foster, Anthony Hopkins',
                        'Jonathan Demme',
                        'Thomas Harris, Ted Tally',
                        'Crime, Drama, Thriller',
                        'https://www.youtube.com/embed/W6Mm8Sbe__o',
                        '1h 58m',
                        'R');
                        
#theatre table

insert into theatre (location) value ("Alba Iulia");
insert into theatre (location) value ("Cluj-Napoca");

#screen table

insert into screen (theatre, screenname, nbrows, nbchairsrow) values (1, '1', 11, 10);
insert into screen (theatre, screenname, nbrows, nbchairsrow) values (1, '2', 10, 10);
insert into screen (theatre, screenname, nbrows, nbchairsrow) values (1, '3', 8, 10);

insert into screen (theatre, screenname, nbrows, nbchairsrow) values (2, '1', 12, 12);
insert into screen (theatre, screenname, nbrows, nbchairsrow) values (2, '2', 12, 10);
insert into screen (theatre, screenname, nbrows, nbchairsrow) values (2, '3', 11, 10);
insert into screen (theatre, screenname, nbrows, nbchairsrow) values (2, '4', 10, 10);


#representation table
#Alba Iulia cinema
#today
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (1, 1, '12:00', 1, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (2, 1, '15:00', 1, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (2, 1, '17:00', 3, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (3, 1, '12:00', 2, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (3, 1, '20:00', 3, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (4, 1, '21:00', 1, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (5, 1, '18:00', 1, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (6, 1, '18:00', 2, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (6, 1, '14:30', 3, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (7, 1, '15:00', 2, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (8, 1, '20:30', 2, '2D');

#Cluj-Napoca cinema
#today
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (1, 1, '12:30', 4, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (1, 1, '16:00', 6, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (2, 1, '12:00', 5, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (2, 1, '19:00', 6, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (3, 1, '15:30', 4, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (4, 1, '21:00', 5, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (4, 1, '16:00', 7, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (5, 1, '18:30', 4, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (5, 1, '13:00', 6, '3D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (6, 1, '18:00', 5, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (6, 1, '13:30', 7, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (7, 1, '15:00', 5, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (7, 1, '19:00', 7, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (8, 1, '21:30', 4, '2D');
insert into representation (movie, dayofweek, starttime, screen, typeofplay) values (8, 1, '22:00', 6, '2D');

#price

insert into price (amount, typeofplay) values (15, '2D');
insert into price (amount, typeofplay) values (20, '3D');
insert into price (amount, typeofplay) values (25, '4DX');

#customer

insert into customer (username, password, firstname, lastname, usertype) values ('dorianpopa', 'parola', 'Dorian', 'Popa', 1);
insert into customer (username, password, firstname, lastname, usertype) values ('~lucia', 'parola1', 'Vacariu', 'Lucia', 2);
insert into customer (username, password, firstname, lastname, usertype) values ('~robert', 'parola2', 'Varga', 'Robert', 2);
insert into customer (username, password, firstname, lastname, usertype) values ('vasilepop', 'parola3', 'Vasile', 'Pop', 3);

-- select * from representation where dayofweek = 1 and (select theatre from screen where idscreen = representation.screen) = 1;

-- select idscreen from screen where theatre = (select idtheatre from theatre where location = "Cluj-Napoca") and screenname = "3";

-- DELIMITER $$ 
-- SET FOREIGN_KEY_CHECKS=0; 
-- delete from movie where title = 'a'; 
-- SET FOREIGN_KEY_CHECKS=1; 
-- $$ 
-- DELIMITER ;

-- update ticket set price = (select amount from price where typeofplay = (select typeofplay from representation where idrepresentation = representation)) where idticket = 2;

-- insert into booking (customer, representation, seatrow, seatcol) values (1,1,1,1);