# --- !Ups

alter table "songs"
add column raagam varchar,
add column taalam varchar,
add column lyrics varchar;

# --- !Downs

alter table "songs"
drop column raagam cascade,
drop column taalam cascade,
drop column lyrics cascade;
