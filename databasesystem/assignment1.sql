-- 1.Grass 타입의 포켓몬을 사전순으로 출력
select name
from Pokemon
where Pokemon.type = "Grass"
order by name;

-- 2.Brown City 또는 Rainbow City 출신 트레이너의 이름을 사전순으로 출력하세요
select name
from Trainer
where Trainer.hometown = "Brown City" || Trainer.hometown = "Rainbow City"
order by name;

-- 3.모든 포켓몬의 type을 중복없이 사전순으로 출력하세요
select distinct type
from Pokemon
order by type;

-- 4.도시의 이름이 B로 시작하는 모든 도시의 이름을 사전순으로 출력하세요
select name
from City
where City.name like "B%"
order by name;

-- 5.이름이 M으로 시작하지 않는 트레이너의 고향을 사전순으로 출력하세요
select hometown
from Trainer
where Trainer.name not in (
select name
from Trainer 
where Trainer.name like "M%")
order by hometown;

-- 6.잡힌 포켓몬 중 가장 레벨이 높은 포켓몬의 별명을 사전순으로 출력하세요
select nickname
from CatchedPokemon
where CatchedPokemon.level in (
select Max(level)
from CatchedPokemon
)
order by nickname;


-- 7.포켓몬의 이름이 알파벳 모음으로 시작하는 포켓몬의 이름을 사전순으로 출력하세요
select name
from Pokemon
where (name LIKE 'I%' OR name LIKE 'E%' OR name LIKE 'A%'OR name LIKE 'O%'OR name LIKE 'U%')
order by name;

-- 8.잡힌 포켓몬의 평균 레벨을 출력하세요
select avg(level)
from CatchedPokemon;

-- 9. Yellow가 잡은 포켓몬의 최대 레벨을 출력하세요
select max(level)
from CatchedPokemon
where CatchedPokemon.owner_id in (
select id
from Trainer
where name ="Yellow"
);

-- 10.트레이너의 고향 이름을 중복없이 사전순으로 출력하세요
select distinct hometown
from Trainer
order by hometown;

-- 11.닉네임이 A로 시작하는 포켓몬을 잡은 트레이너의 이름과 포켓몬의 닉네임을 트레이너의 이름의 사전순으로 출력하세요
select name, nickname
from CatchedPokemon as C, Trainer as T
where C.nickname like "A%" and C.owner_id = T.id
order by T.name;


-- 12. Amazon 특성을 가진 도시의 리더의 트레이너 이름을 출력하세요
select name
from Trainer
where Trainer.id = 







