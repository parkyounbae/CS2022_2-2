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
where Trainer.id in (
select leader_id
from Gym
where city in (
select name
from City 
where description = "Amazon"
)
);

-- 13. 불속성 포켓몬을 가장 많이 잡은 트레이너의 id와, 그 트레이너가 잡은 불속성 포켓몬의 수를 출력하세
select owner_id, count(*) as fireCount
from CatchedPokemon left join Pokemon on CatchedPokemon.pid = Pokemon.id
where type = "Fire"
group by owner_id
order by fireCount desc
limit 1;

-- 14. 포켓몬 ID가 한 자리 수인 포켓몬의 type을 중복 없이 포켓몬 ID의 내림차순으로 출력하세요
select type
from Pokemon
where id < 10
group by type
order by min(id) desc;

-- 15. 포켓몬의 type이 Fire이 아닌 포켓몬의 수를 출력하세요
select count(*)
from Pokemon
where type != "Fire";


-- 16. 진화하면id가작아지는포켓몬의진화전이름을사전순으로출력하세요
select name
from Evolution join Pokemon on Evolution.before_id = Pokemon.id
where before_id > after_id
order by name;

-- 17. 트레이너에게 잡힌 모든 물속성 포켓몬의 평균 레벨을 출력하세요
select avg(level)
from CatchedPokemon join Pokemon on CatchedPokemon.pid = Pokemon.id
where type = "Water";

-- 18. 체육관 리더가 잡은 모든 포켓몬 중 레벨이 가장 높은 포켓몬의 별명을 출력하세요
select max(level)
from CatchedPokemon join Gym on CatchedPokemon.owner_id = Gym.leader_id;

-- 19. Blue city 출신 트레이너들 중 잡은 포켓몬들의 레벨의 평균이 가장 높은 트레이너의 이름을 사전순으로 출력하세요
select name
from
(
select name, avg(level)
from CatchedPokemon join Trainer on CatchedPokemon.owner_id = Trainer.id
where hometown = "Blue City"
group by name
) as tempTable
order by name;

-- 20. 같은 출신이 없는 트레이너들이 잡은 포켓몬중 진화가 가능하고 Electric 속성을 가진 포켓몬의 이름을 출력하세요
with tempTable as (
select pokeName, hometown
from (select name as pokeName, pid, owner_id
from CatchedPokemon join Pokemon on CatchedPokemon.pid = Pokemon.id
where type = "Electric" and pid in (
select before_id
from Evolution)) as temp join Trainer on owner_id = Trainer.id
)
select pokeName
from tempTable
group by pokeName
having count(*) < 2;

-- 21. 관장들의 이름과 각 관장들이 잡은 포켓몬들의 레벨 합을 레벨 합의 내림차 순으로 출력하세요
select sum(level)
from (CatchedPokemon join Gym on CatchedPokemon.owner_id = Gym.leader_id as temp) join Trainer on 
group by owner_id
order by sum(level) desc;

-- 22. 가장 트레이너가 많은 고향의 이름을 출력하세요.






-- 23. Sangnok City 출신 트레이너와 Brown City 출신 트레이너가 공통으로 잡은 포켓몬의 이름을 중복을 제거하여 사전순으로 출력
-- 24. 이름이 P로 시작하는 포켓몬을 잡은 트레이너 중 상록 시티 출신인 트레이너의 이름을 사전순으로 모두 출력하세요
-- 25. 트레이너의 이름과 그 트레이너가 잡은 포켓몬의 이름을 출력하세요. 이때 트레이너 이름은 사전 순으로 정렬하고, 각 트레이너가 잡은 포켓몬도 사전 순으로 정렬하세요.
-- 26. 2단계 진화만 가능한 포켓몬의 이름을 사전순으로 출력하세요
-- 27. 상록 시티의 관장이 잡은 포켓몬들 중 포켓몬의 타입이 WATER 인 포켓몬의 별명을 사전순으로 출력 하세요
-- 28. 트레이너들이 잡은 포켓몬 중 진화한 포켓몬이 3마리 이상인 경우 해당 트레이너의 이름을 사전순으로 출력하세요
-- 29. 어느 트레이너에게도 잡히지 않은 포켓몬의 이름을 사전 순으로 출력하세요
-- 30. 각 출신 도시별로 트레이너가 잡은 포켓몬중 가장 레벨이 높은 포켓몬의 레벨을 내림차순으로 출력 하세요.
-- 31. 포켓몬 중 3단 진화가 가능한 포켓몬의 ID 와 해당 포켓몬의이름을 1단진화 형태 포켓몬의이름, 2단진화 형태 포켓몬의 이름, 3단 진화 형태 포켓몬의 이름을 ID 의 오름차순으로 출력하세요



