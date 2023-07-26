use busdb;

CREATE TABLE tbl_ter(
                        terId           VARCHAR(10)     PRIMARY KEY,
                        terName         VARCHAR(30)     NOT NULL,
                        terRegion       VARCHAR(10),
                        terCoorX        VARCHAR(30),
                        terCoorY        VARCHAR(30)
);

CREATE TABLE tbl_TerLink(
                            tl_Id              VARCHAR(20)    PRIMARY KEY,
                            tl_depTerId        VARCHAR(10)    NOT NULL,
                            tl_arrTerId        VARCHAR(10)    NOT NULL
);

CREATE TABLE tbl_TerDrive(
                             td_Id               VARCHAR(20)    PRIMARY KEY,
                             td_TlId             VARCHAR(20)    NOT NULL,
                             td_interval         VARCHAR(6),
                             td_wasteTime        VARCHAR(6),
                             td_fare             INT
);

CREATE TABLE tbl_Terschedule(
                                tes_TdId            VARCHAR(20)    NOT NULL,
                                tes_schedule        VARCHAR(6)
);

select * from tbl_ter;

select * from tbl_TerLink;

select * from tbl_terdrive;

select * from tbl_terschedule;

desc tbl_terlink;


-- truncate tbl_ter;
-- truncate tbl_terlink;
-- truncate tbl_terdrive;
-- truncate tbl_terschedule;

SELECT * FROM busdb.tbl_terlink where tl_depTerId = 3601689 AND tl_arrTerId = 4000077;

SELECT tl_Id, tl_depTerId, tl_arrTerId, terId, terName, terRegion
FROM tbl_TerLink TL
         LEFT JOIN tbl_ter T ON T.terId = TL.tl_depTerId
UNION
SELECT tl_Id, tl_depTerId, tl_arrTerId, terId, terName, terRegion
FROM tbl_TerLink TL
         LEFT JOIN tbl_ter T ON T.terId = TL.tl_arrTerId;

select * from busdb.tbl_terschedule S
            left join busdb.tbl_terdrive D ON S.tes_TdId = D.td_Id;

