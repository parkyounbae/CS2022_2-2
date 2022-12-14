# create table
CREATE TABLE City (
  name varchar(32),
  description varchar(32),
  primary key (name)
 );
 
 CREATE TABLE Trainer (
   id INT(11) unsigned AUTO_INCREMENT,
   name varchar(32),
   hometown varchar(32),
   primary key (id),
   foreign key (hometown) references City (name)
 );
 
 CREATE TABLE Gym (
   leader_id INT(11) unsigned,
   city varchar(32),
   foreign key (leader_id) references Trainer (id),
   foreign key (city) references City (name)
 );
 
 CREATE TABLE Pokemon (
   id INT(11) unsigned,
   name varchar(32),
   type varchar(32),
   primary key (id)
 );
 
 CREATE TABLE CatchedPokemon (
   id INT(11) unsigned auto_increment,
   owner_id INT(11) unsigned,
   pid INT(11) unsigned,
   level INT(11) unsigned,
   nickname varchar(32),
   primary key (id),
   foreign key (owner_id) references Trainer (id),
   foreign key (pid) references Pokemon (id)
 );
 
 CREATE TABLE Evolution (
   before_id INT(11) unsigned,
   after_id INT(11) unsigned,
   foreign key (before_id) references Pokemon (id),
   foreign key (after_id) references Pokemon (id)
 );
 
# insert.sql

INSERT INTO City (name, description) VALUES
('Blue City', 'Waterpark'),
('Sangnok City', 'Amazon'),
('Brown City', 'Famous Harbor'),
('Rainbow City', 'Huge City with Casino');

INSERT INTO Trainer (name, hometown) VALUES
('Red', 'Sangnok City'),
('Green', 'Sangnok City'),
('Blue', 'Sangnok City'),
('Ash', 'Sangnok City'),
('Yellow', 'Sangnok City'),
('Gold', 'Blue City'),
('Matis', 'Brown City'),
('Erika', 'Rainbow City'),
('Misty', 'Blue City');

INSERT INTO Gym (leader_id, city) VALUES
((SELECT id from Trainer where name='Misty'), 'Blue City'),
((SELECT id from Trainer where name='Green'), 'Sangnok City'),
((SELECT id from Trainer where name='Matis'), 'Brown City'),
((SELECT id from Trainer where name='Erika'), 'Rainbow City');

INSERT INTO Pokemon (id, name, type) VALUES
(1, 'Bulbasaur', 'Grass'),
(2, 'Ivysaur', 'Grass'),
(3, 'Venusaur', 'Grass'),
(4, 'Charmander', 'Fire'),
(5, 'Charmeleon', 'Fire'),
(6, 'Charizard', 'Fire'),
(7, 'Squirtle', 'Water'),
(8, 'Wartortle', 'Water'),
(9, 'Blastoise', 'Water'),
(17, 'Pidgeotto', 'Normal'),
(18, 'Pidgeot', 'Normal'),
(25, 'Pikachu', 'Electric'),
(26, 'Raichu', 'Electric'),
(37, 'Vulpix', 'Fire'),
(38, 'Ninetales', 'Fire'),
(46, 'Paras', 'Grass'),
(47, 'Parasect', 'Grass'),
(54, 'Psyduck', 'Water'),
(55, 'Golduck', 'Water'),
(84, 'Doduo', 'Normal'),
(85, 'Dodrio', 'Normal'),
(116, 'Horsea', 'Water'),
(117, 'Seadra', 'Water'),
(126, 'Magmar', 'Fire'),
(129, 'Magikarp', 'Water'),
(130, 'Gyarados', 'Water'),
(131, 'Lapras', 'Water'),
(133, 'Eevee', 'Normal'),
(137, 'Porygon', 'Normal'),
(150, 'Mewtwo', 'Psychic'),
(151, 'Mew', 'Psychic'),
(152, 'Chikorita', 'Grass'),
(172, 'Pichu', 'Electric'),
(249, 'Lugia', 'Psychic'),
(251, 'Celebi', 'Psychic');

INSERT INTO Evolution (before_id, after_id) VALUES
(1,2),
(2,3),
(4,5),
(5,6),
(7,8),
(8,9),
(17,18),
(25,26),
(37,38),
(46,47),
(54,55),
(84,85),
(116,117),
(129,130),
(172,25);

INSERT INTO CatchedPokemon (pid, owner_id, level, nickname) VALUES
(6, (SELECT id from Trainer where name='Green'), 70, 'After eating hot peppers'),
(3, (SELECT id from Trainer where name='Red'), 80, 'Funny Smell'),
(25, (SELECT id from Trainer where name='Red'), 10, 'AAA'),
(25, (SELECT id from Trainer where name='Ash'), 50, 'Mouse'),
(26, (SELECT id from Trainer where name='Red'), 60, 'AA'),
(131, (SELECT id from Trainer where name='Misty'), 80, 'Laputa'),
(131, (SELECT id from Trainer where name='Ash'), 50, 'Turtle'),
(133, (SELECT id from Trainer where name='Green'), 5, 'Fluffy'),
(150, (SELECT id from Trainer where name='Red'), 100, 'Spoon killer'),
(152, (SELECT id from Trainer where name='Gold'), 1, 'Salad'),
(152, (SELECT id from Trainer where name='Gold'), 2, 'Piece of cake'),
(8, (SELECT id from Trainer where name='Blue'), 20, 'Ninja Turtle'),
(117, (SELECT id from Trainer where name='Blue'), 20, 'Water gun'),
(38, (SELECT id from Trainer where name='Green'), 50, 'Ahri'),
(55, (SELECT id from Trainer where name='Green'), 65, 'Donald Duck'),
(26, (SELECT id from Trainer where name='Yellow'), 60, 'First friend'),
(84, (SELECT id from Trainer where name='Yellow'), 3, 'Cho\'gall'),
(85, (SELECT id from Trainer where name='Yellow'), 3, 'Three-headed dumb'),
(46, (SELECT id from Trainer where name='Erika'), 30, 'Mushroom'),
(152, (SELECT id from Trainer where name='Erika'), 5, 'Chikorita'),
(130, (SELECT id from Trainer where name='Misty'), 90, 'Worm'),
(25, (SELECT id from Trainer where name='Matis'), 40, 'Free Battery'),
(26, (SELECT id from Trainer where name='Matis'), 50, 'Huge Battery'),
(172, (SELECT id from Trainer where name='Matis'), 10, 'Small Battery'),
(130, (SELECT id from Trainer where name='Red'), 95, 'Big yawn');