-- public.city definition
     	DROP TABLE city;
        CREATE TABLE city (
        id bigserial,
        "name" varchar(255) NOT NULL,
        state varchar(2) NOT NULL,
        CONSTRAINT city_pk PRIMARY KEY (id)
        );
-- public.contact definition
  -- Drop table
  	  DROP TABLE contact;
      CREATE TABLE contact (
        id bigserial,
        city_id serial,
        "name" varchar(255) NULL,
        gender varchar(255) NULL,
        birth date NULL,
        age int2 NULL,
        CONSTRAINT contact_pk PRIMARY KEY (id)
      );
  -- public.contact foreign keys
     ALTER TABLE public.contact ADD CONSTRAINT contact_fk FOREIGN KEY (city_id) REFERENCES city(id) ON UPDATE RESTRICT ON DELETE RESTRICT;