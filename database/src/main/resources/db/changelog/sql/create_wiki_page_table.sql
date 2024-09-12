create sequence if not exists wiki.wiki_source_seq NO MAXVALUE START WITH 1 INCREMENT BY 1 NO CYCLE;
create table if not exists wiki.wiki_source(
    id bigint not null primary key default nextval('wiki.wiki_source_seq'),
    source varchar(500) unique not null,
    created_at timestamp with time zone not null
);

create sequence if not exists wiki.wiki_page_seq NO MAXVALUE START WITH 1 INCREMENT BY 1 NO CYCLE;
create table if not exists wiki.wiki_page(
    id bigint not null primary key default nextval('wiki.wiki_page_seq'),
    title varchar(200) not null,
    revision varchar(50) not null,
    page_text text not null,
    source_id bigint not null references wiki.wiki_source(id)
);
