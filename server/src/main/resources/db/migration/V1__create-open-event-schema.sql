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
    CONSTRAINT cache_settings_name UNIQUE (name)
);

/* user */
CREATE SEQUENCE db_user_id_seq;
CREATE TABLE db_user
(
    id           bigint       not null primary key default nextval('db_user_id_seq'::regclass),
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
    CONSTRAINT db_user_external_id UNIQUE (external_id)
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
        REFERENCES db_user (id) MATCH SIMPLE
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
CREATE TABLE event
(
    id             bigint  not null,
    period_start   TIMESTAMP WITHOUT TIME ZONE,
    period_end     TIMESTAMP WITHOUT TIME ZONE,
    published      boolean NOT NULL,

    owner_id       bigint,
    description_id bigint,
    location_id    bigint,

    created        TIMESTAMP WITHOUT TIME ZONE,
    modified       TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT event_pk UNIQUE (id),
    CONSTRAINT fk_event_user FOREIGN KEY (owner_id)
        REFERENCES db_user (id) MATCH SIMPLE,
    CONSTRAINT fk_event_description FOREIGN KEY (description_id)
        REFERENCES item_description (id) MATCH SIMPLE,
    CONSTRAINT fk_event_location FOREIGN KEY (location_id)
        REFERENCES location (id) MATCH SIMPLE
        ON DELETE SET NULL
);

/* structure */
CREATE TABLE structure
(
    id                  bigint  not null,
    root                boolean NOT NULL,
    visible             boolean NOT NULL,
    auto_accept_viewer  boolean NOT NULL,
    restricted          boolean NOT NULL,
    parent_structure_id bigint,

    owner_id            bigint,
    location_id         bigint,

    created             TIMESTAMP WITHOUT TIME ZONE,
    modified            TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT structure_pk UNIQUE (id),
    CONSTRAINT fk_structure_user FOREIGN KEY (owner_id)
        REFERENCES db_user (id) MATCH SIMPLE,
    CONSTRAINT fk_structure_description FOREIGN KEY (id)
        REFERENCES item_description (id) MATCH SIMPLE,
    CONSTRAINT fk_structure_location FOREIGN KEY (location_id)
        REFERENCES location (id) MATCH SIMPLE
        ON DELETE SET NULL,
    CONSTRAINT fk_structure_parent_structure FOREIGN KEY (parent_structure_id)
        REFERENCES structure (id) MATCH SIMPLE
);

/* category */
CREATE SEQUENCE category_seq;
CREATE TABLE category
(
    id       bigint                 not null primary key default nextval('category_seq'::regclass),
    name     character varying(255) NOT NULL,
    icon_url character varying(255) NOT NULL,

    created  TIMESTAMP WITHOUT TIME ZONE,
    modified TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT category_pk UNIQUE (id)
);

CREATE TABLE category_item_relation
(
    category_id bigint not null,
    item_id     bigint not null,
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES category (id) MATCH SIMPLE
);

/* announcement */
CREATE SEQUENCE announcement_seq;
CREATE TABLE announcement
(
    id        bigint                 not null primary key default nextval('announcement_seq'::regclass),
    subject   character varying(255) NOT NULL,
    content   TEXT                   NOT NULL,

    author_id bigint,
    item_id   bigint,

    created   TIMESTAMP WITHOUT TIME ZONE,
    modified  TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT announcement_pk UNIQUE (id),
    CONSTRAINT fk_announcement_user FOREIGN KEY (author_id)
        REFERENCES db_user (id) MATCH SIMPLE
);

/* message */
CREATE SEQUENCE message_seq;
CREATE TABLE message
(
    id                bigint                 not null primary key default nextval('message_seq'::regclass),
    subject           character varying(255) NOT NULL,
    content           TEXT                   NOT NULL,
    status            character varying(255) NOT NULL,

    author_id         bigint,
    recipient_id      bigint,
    item_id           bigint,
    parent_message_id bigint,

    created_timestamp TIMESTAMP WITHOUT TIME ZONE,
    read_timestamp    TIMESTAMP WITHOUT TIME ZONE,
    replied_timestamp TIMESTAMP WITHOUT TIME ZONE,
    modified          TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT message_pk UNIQUE (id),
    CONSTRAINT fk_message_author FOREIGN KEY (author_id)
        REFERENCES db_user (id) MATCH SIMPLE,
    CONSTRAINT fk_message_recipient FOREIGN KEY (recipient_id)
        REFERENCES db_user (id) MATCH SIMPLE,
    CONSTRAINT fk_parent_message FOREIGN KEY (parent_message_id)
        REFERENCES message (id) MATCH SIMPLE,
    CONSTRAINT fk_message_item FOREIGN KEY (item_id)
        REFERENCES item_description (id) MATCH SIMPLE
);

/* entry process */
CREATE SEQUENCE entry_process_seq;
CREATE TABLE entry_process
(
    id          bigint                 not null primary key default nextval('entry_process_seq'::regclass),
    type        character varying(255) NOT NULL,
    status      character varying(255) NOT NULL,
    entitlement character varying(255) NOT NULL,

    user_id     bigint,
    item_id     bigint,

    created     TIMESTAMP WITHOUT TIME ZONE,
    modified    TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT entry_process_pk UNIQUE (id),
    CONSTRAINT fk_entry_process_user FOREIGN KEY (user_id)
        REFERENCES db_user (id) MATCH SIMPLE
);

/* member */
CREATE SEQUENCE member_seq;
CREATE TABLE member
(
    id          bigint                 not null primary key default nextval('member_seq'::regclass),
    entitlement character varying(255) NOT NULL,
    contact     boolean                NOT NULL,

    user_id     bigint,
    item_id     bigint,

    created     TIMESTAMP WITHOUT TIME ZONE,
    modified    TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT member_pk UNIQUE (id),
    CONSTRAINT fk_member_user FOREIGN KEY (user_id)
        REFERENCES db_user (id) MATCH SIMPLE
);
