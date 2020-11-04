# Spring Boot - Bootstrap App
### Objetivo
Implementar uma API Rest para cadastro (CRUD) de Pessoas de Contato e Cidades.

### Pré-Requisitos
1. Instalar o Postgres na sua máquina
1. Criar a database contactdb ou outro nome a sua escolha parametrizando no application.properties.
1. Configurar a credencial de acesso no application.properties.
1. Criar as tabelas da contactdb com script abaixo:
 `-- public.city definition
  -- Drop table
  -- DROP TABLE city;
        CREATE TABLE city (
        id bigserial NOT NULL DEFAULT nextval('city_id_seq'::regclass),
        "name" varchar(255) NOT NULL,
        state varchar(2) NOT NULL,
        CONSTRAINT city_pk PRIMARY KEY (id)
        );
  -- public.contact definition  
  -- Drop table
  -- DROP TABLE contact;
      CREATE TABLE contact (
        id bigserial NOT NULL DEFAULT nextval('contact_id_seq'::regclass),
        city_id int8 NOT NULL DEFAULT nextval('contact_city_seq'::regclass),
        "name" varchar(255) NULL,
        gender varchar(255) NULL,
        birth date NULL,
        age int2 NULL,
        CONSTRAINT contact_pk PRIMARY KEY (id)
      );
  -- public.contact foreign keys
    ALTER TABLE public.contact ADD CONSTRAINT contact_fk FOREIGN KEY (city_id) REFERENCES city(id) ON UPDATE RESTRICT ON DELETE RESTRICT;`