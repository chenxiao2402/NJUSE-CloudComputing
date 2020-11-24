drop table if exists author_citations;
drop table if exists paper_citations;


create table author_citations
(
	year int not null,
	author varchar(128) not null,
	subject varchar(128) not null,
	citations int not null,
	constraint author_citations_pk
		primary key (year, author, subject)
);

create table paper_citations
(
	title varchar(256) not null,
	year int not null,
	month int not null,
	citations int not null,
	subject varchar(256) not null,
	constraint paper_citations_pk
		primary key (title, subject)
);

