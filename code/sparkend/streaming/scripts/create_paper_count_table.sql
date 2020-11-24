drop table if exists subject_paper_count;

create table subject_paper_count
(
    month int not null,
    year int not null,
    subject varchar(128) not null,
    paper_count int not null,
    author_count int not null,
    constraint subject_paper_count_pk
        primary key (month, year, subject)
);