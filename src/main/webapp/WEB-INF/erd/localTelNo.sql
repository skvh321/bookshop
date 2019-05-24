create table localTel(
seq number(3) primary key,
localNo varchar2(4),
description varchar2(30)
);

truncate table localTel;
drop sequence localtel_seq;

create sequence localtel_seq increment by 1 start with 1;


insert into localTel(seq,localNo) values(localtel_seq.nextval, '02');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '031');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '032');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '033');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '041');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '042');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '043');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '051');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '052');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '053');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '054');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '055');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '061');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '062');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '063');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '064');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0502');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0503');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0505');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0506');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0507');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '0508');
insert into localTel(seq,localNo) values(localtel_seq.nextval, '070');


select localNo from localTel;