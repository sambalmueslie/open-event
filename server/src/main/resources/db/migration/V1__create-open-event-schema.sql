/* cache settings */
CREATE SEQUENCE cache_settings_seq;
CREATE TABLE cache_settings
(
    id       bigint       not null primary key default nextval('cache_settings_seq'::regclass),
    name     varchar(255) NOT NULL,
    enabled  boolean      NOT NULL,

    created  TIMESTAMP WITHOUT TIME ZONE,
    modified TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT cache_settings_pk UNIQUE (id),
    constraint cache_settings_name unique (name)
);

/* user */
CREATE SEQUENCE _user_id_seq;
CREATE TABLE _user
(
    id           bigint       not null primary key default nextval('_user_id_seq'::regclass),
    external_id  varchar(255) NOT NULL,
    user_name    varchar(255) NOT NULL,
    first_name   varchar(255) NOT NULL,
    last_name    varchar(255) NOT NULL,
    email        varchar(255) NOT NULL,
    icon_url     varchar(255) NOT NULL,
    last_sync    timestamp,
    service_user boolean      NOT NULL,

    created      TIMESTAMP WITHOUT TIME ZONE,
    modified     TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT user_pk UNIQUE (id),
    constraint   _user_external_id unique (external_id)
);


/* item description */
CREATE SEQUENCE item_description_seq;
CREATE TABLE item_description
(
    id         bigint       not null primary key default nextval('item_description_seq'::regclass),
    title      varchar(255) NOT NULL,
    short_text varchar(255) NOT NULL,
    long_text  text         NOT NULL,
    image_url  varchar(255) NOT NULL,
    icon_url   varchar(255) NOT NULL,

    created    TIMESTAMP WITHOUT TIME ZONE,
    modified   TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT item_description_pk UNIQUE (id)
);

/* item entitlement entry */
CREATE SEQUENCE item_entitlement_entry_seq;
CREATE TABLE item_entitlement_entry
(
    id          bigint not null primary key default nextval('item_entitlement_entry_seq'::regclass),
    item_id     bigint not null,
    type        character varying(255),
    entitlement character varying(255),

    user_id     bigint,

    created     TIMESTAMP WITHOUT TIME ZONE,
    modified    TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT item_entitlement_entry_pk UNIQUE (id),
    CONSTRAINT fk_item_entitlement_entry_user FOREIGN KEY (user_id)
        REFERENCES _user (id) MATCH SIMPLE
);

/* address */
CREATE SEQUENCE address_seq;
CREATE TABLE address
(
    id              bigint not null primary key default nextval('address_seq'::regclass),
    additional_info character varying(255),
    city            character varying(255),
    country         character varying(255),
    street          character varying(255),
    street_number   character varying(255),
    zip             character varying(255),

    created         TIMESTAMP WITHOUT TIME ZONE,
    modified        TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT address_pk UNIQUE (id)
);

/* geo_location */
CREATE SEQUENCE geo_location_seq;
CREATE TABLE geo_location
(
    id       bigint not null primary key default nextval('geo_location_seq'::regclass),
    lat      double precision,
    lon      double precision,

    created  TIMESTAMP WITHOUT TIME ZONE,
    modified TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT geo_location_pk UNIQUE (id)
);

/* location_properties */
CREATE SEQUENCE location_properties_seq;
CREATE TABLE location_properties
(
    id       bigint not null primary key default nextval('location_properties_seq'::regclass),
    size     integer                     default 0,

    created  TIMESTAMP WITHOUT TIME ZONE,
    modified TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT location_properties_pk UNIQUE (id)
);

/* location */
CREATE SEQUENCE location_seq;
CREATE TABLE location
(
    id              bigint not null primary key default nextval('location_seq'::regclass),

    address_id      bigint,
    geo_location_id bigint,
    properties_id   bigint,

    created         TIMESTAMP WITHOUT TIME ZONE,
    modified        TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT location_pk UNIQUE (id)
);


/* event */
CREATE SEQUENCE event_seq;
CREATE TABLE event
(
    id             bigint not null primary key default nextval('event_seq'::regclass),
    period_start   TIMESTAMP WITHOUT TIME ZONE,
    period_end     TIMESTAMP WITHOUT TIME ZONE,

    owner_id       bigint,
    description_id bigint,
    location_id    bigint,

    created        TIMESTAMP WITHOUT TIME ZONE,
    modified       TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT event_pk UNIQUE (id),
    CONSTRAINT fk_event_user FOREIGN KEY (owner_id)
        REFERENCES _user (id) MATCH SIMPLE,
    CONSTRAINT fk_event_description FOREIGN KEY (description_id)
        REFERENCES item_description (id) MATCH SIMPLE,
    CONSTRAINT fk_event_location FOREIGN KEY (location_id)
        REFERENCES location (id) MATCH SIMPLE
        ON DELETE SET NULL
);
