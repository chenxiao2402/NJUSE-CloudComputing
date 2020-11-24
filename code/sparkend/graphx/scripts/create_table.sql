create table subject_crossover_rank
(
    start_year     int          not null,
    subject        varchar(128) not null,
    crossover_rank double       not null,
    primary key (start_year, subject)
);

create table relative_subjects
(
    start_year  int          not null,
    subject1    varchar(128) not null,
    subject2    varchar(128) not null,
    paper_count int          not null,
    primary key (start_year, subject1, subject2)
);

create table author_connections_author
(
    start_year      int          not null,
    subject         varchar(128) not null,
    author_id       bigint       not null,
    author_name     varchar(64)  not null,
    paper_count     int          not null,
    author_category bigint       not null,
    primary key (start_year, subject, author_id)
);

create table collaborations
(
    id            bigint         auto_increment primary key,
    start_year    int            not null,
    subject       varchar(128)   not null,
    source_author bigint         not null,
    target_author bigint         not null,
    primary key id
);

create table collaboration_path
(
    id            bigint auto_increment
        primary key,
    start_year    int           not null,
    subject       varchar(128)  not null,
    source_author varchar(64)   not null,
    target_author varchar(64)   not null,
    path          varchar(1024) null comment 'data example=author1,author2,author3'
);