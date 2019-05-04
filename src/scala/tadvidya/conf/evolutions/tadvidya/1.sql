# --- !Ups

create table "songs" (
                          "id" BIGSERIAL PRIMARY KEY,
                          "title" VARCHAR NOT NULL UNIQUE,
                          "composer" VARCHAR NOT NULL,
                          "language" VARCHAR NOT NULL
);

# --- !Downs

drop table "songs" if exists;
