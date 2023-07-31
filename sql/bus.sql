use busdb;

CREATE TABLE tbl_ter
(
    terId     VARCHAR(10) PRIMARY KEY,
    terName   VARCHAR(30) NOT NULL,
    terRegion VARCHAR(10),
    terCoorX  VARCHAR(30),
    terCoorY  VARCHAR(30)
);

CREATE TABLE tbl_TerLink
(
    tl_Id       VARCHAR(20) PRIMARY KEY,
    tl_depTerId VARCHAR(10) NOT NULL,
    tl_arrTerId VARCHAR(10) NOT NULL
);

CREATE TABLE tbl_TerDrive
(
    td_Id        VARCHAR(20) PRIMARY KEY,
    td_TlId      VARCHAR(20) NOT NULL,
    td_interval  VARCHAR(6),
    td_wasteTime VARCHAR(6),
    td_fare      INT
);

CREATE TABLE tbl_Terschedule
(
    tes_TdId     VARCHAR(20) NOT NULL,
    tes_schedule VARCHAR(6)
);

CREATE TABLE tbl_bus_user
(
    bu_id       VARCHAR(20) PRIMARY KEY,
    bu_password VARCHAR(125) NOT NULL,
    bu_name     VARCHAR(125) NOT NULL,
    bu_tel      VARCHAR(15)  NOT NULL
);
desc table tbl_bus_user;
select * from tbl_bus_user;

insert into tbl_bus_user(bu_id, bu_password, bu_name, bu_tel) VALUES ('test',1234,'testaccount',010-1111-1111);


select *
from tbl_ter;

select *
from tbl_TerLink;

select *
from tbl_terdrive;

select *
from tbl_terschedule;

desc tbl_terlink;


-- truncate tbl_ter;
-- truncate tbl_terlink;
-- truncate tbl_terdrive;
-- truncate tbl_terschedule;

SELECT *
FROM busdb.tbl_terlink
where tl_depTerId = 3601689
  AND tl_arrTerId = 4000077;

# linkTer, ter join
SELECT tl_Id, tl_depTerId, D.terName AS depTerName, tl_arrTerId, A.terName AS arrTerName
FROM tbl_TerLink TL
         LEFT JOIN tbl_ter D ON D.terId = TL.tl_depTerId
         LEFT JOIN tbl_ter A ON A.terId = TL.tl_arrTerId;

#terDrive, terSchedule join
SELECT td_Id, td_TlId, td_interval, td_wasteTime, td_fare, tes_schedule
FROM tbl_terdrive
    LEFT JOIN tbl_terschedule ON tes_TdId = td_Id;


select *
from busdb.tbl_terschedule S
         left join busdb.tbl_terdrive D ON S.tes_TdId = D.td_Id;

SELECT *
FROM busdb.tbl_terschedule
WHERE tes_TdId = '36016894000077_00';

CREATE VIEW view_terLink AS
SELECT tl_Id, tl_depTerId, tl_arrTerId, D.terName AS DepTerName, A.terName AS ArrTerName
FROM tbl_TerLink TL
         LEFT JOIN tbl_ter D ON D.terId = TL.tl_depTerId
         LEFT JOIN tbl_ter A ON A.terId = TL.tl_arrTerId;

select * from view_terlink;

select * from view_terlink where tl_depTerId = '3601689'
order by ArrTerName;

select * from view_terdrive where td_TlId = '36016954000044'
order by tes_schedule;


desc view_terlink;
desc view_terdrive;

CREATE VIEW view_terDrive AS
SELECT td_Id, td_TlId, td_interval, td_wasteTime, td_fare, tes_schedule
FROM tbl_terdrive
         LEFT JOIN tbl_terschedule ON tes_TdId = td_Id;