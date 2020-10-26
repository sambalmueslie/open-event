/* cache settings */
create sequence cache_settings_seq;
create table cache_settings
(
    id bigint not null primary key default nextval('cache_settings_seq'::regclass),
    name varchar(255) not null,
    enabled boolean not null,
    constraint cache_settings_id unique (id),
    constraint cache_settings_name unique (name)
);

/* user */
create sequence _user_id_seq;
create table _user
(
    id           bigint          not null primary key default nextval('_user_id_seq'::regclass),
    external_id  varchar(255) not null,
    user_name    varchar(255) not null,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    email        varchar(255) not null,
    icon_url     varchar(255) not null,
    last_sync    timestamp,
    service_user boolean      not null,
    constraint _user_id unique (id),
    constraint _user_external_id unique (external_id)
);
