/* cache settings */
create table cache_settings
(
    id       bigint GENERATED ALWAYS AS IDENTITY,
    name     varchar(255) NOT NULL,
    enabled  boolean      NOT NULL,

    created  timestamp without time zone,
    modified timestamp without time zone,

    PRIMARY KEY (id),
    constraint cache_settings_name unique (name)
);

/* user */
create table _user
(
    id           bigint GENERATED ALWAYS AS IDENTITY,
    external_id  varchar(255) NOT NULL,
    user_name    varchar(255) NOT NULL,
    first_name   varchar(255) NOT NULL,
    last_name    varchar(255) NOT NULL,
    email        varchar(255) NOT NULL,
    icon_url     varchar(255) NOT NULL,
    last_sync    timestamp,
    service_user boolean      NOT NULL,

    created      timestamp without time zone,
    modified     timestamp without time zone,

    PRIMARY KEY (id),
    constraint   _user_external_id unique (external_id)
);


/* item description */
create table item_description
(
    id         bigint GENERATED ALWAYS AS IDENTITY,
    title      varchar(255) NOT NULL,
    short_text varchar(255) NOT NULL,
    long_text  text         NOT NULL,
    image_url  varchar(255) NOT NULL,
    icon_url   varchar(255) NOT NULL,

    created    timestamp without time zone,
    modified   timestamp without time zone,

    PRIMARY KEY (id)
);

/* address */
create table address
(
    id              bigint GENERATED ALWAYS AS IDENTITY,
    additional_info character varying(255),
    city            character varying(255),
    country         character varying(255),
    street          character varying(255),
    street_number   character varying(255),
    zip             character varying(255),

    created         timestamp without time zone,
    modified        timestamp without time zone,

    PRIMARY KEY (id)
);

/* geo_location */
create table geo_location
(
    id       bigint GENERATED ALWAYS AS IDENTITY,
    lat      double precision,
    lon      double precision,

    created  timestamp without time zone,
    modified timestamp without time zone,

    PRIMARY KEY (id)
);

/* location_properties */
create table location_properties
(
    id       bigint GENERATED ALWAYS AS IDENTITY,
    size     integer default 0,

    created  timestamp without time zone,
    modified timestamp without time zone,

    PRIMARY KEY (id)
);

/* location */
create table location
(
    id              bigint GENERATED ALWAYS AS IDENTITY,

    address_id      bigint,
    geo_location_id bigint,
    properties_id   bigint,

    created         timestamp without time zone,
    modified        timestamp without time zone,

    PRIMARY KEY (id)
);


/* event */
create table event
(
    id             bigint GENERATED ALWAYS AS IDENTITY,
    period_start   timestamp without time zone,
    period_end     timestamp without time zone,

    description_id bigint,
    location_id    bigint,

    created        timestamp without time zone,
    modified       timestamp without time zone,

    PRIMARY KEY (id),
    CONSTRAINT fk_description FOREIGN KEY (description_id)
        REFERENCES item_description (id) MATCH SIMPLE,
    CONSTRAINT fk_location FOREIGN KEY (location_id)
        REFERENCES location (id) MATCH SIMPLE
        ON DELETE SET NULL
);
