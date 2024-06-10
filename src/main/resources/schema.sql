-- need to manual create table with jdbc:h2:mem:///./testdb
create table SUBSCRIBER_MODEL
(
    ID           BIGINT auto_increment primary key,
    NAME         CHARACTER VARYING(255),
    TITLE        CHARACTER VARYING(255),
    DESCRIPTION  CHARACTER VARYING,
    STATUS       CHARACTER VARYING(255),
    RECURRING_AT TIMESTAMP,
    CREATED_AT   TIMESTAMP,
    MODIFIED_AT  TIMESTAMP
);

create table CONFIG_MODEL
(
    ID           BIGINT auto_increment primary key,
    CONFIG_KEY   CHARACTER VARYING(255) UNIQUE,
    CONFIG_VALUE CHARACTER VARYING(255),
    CREATED_AT   TIMESTAMP,
    MODIFIED_AT  TIMESTAMP
);

