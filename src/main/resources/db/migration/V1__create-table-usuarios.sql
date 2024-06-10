create table usuarios(
    id bigint not null auto_increment,
    nome_completo varchar(100) not null,
    cpf varchar(15) not null,
    email varchar(100) not null,
    senha varchar(100) not null,
    saldo decimal(10,2) not null,
    tipo_usuario varchar(20) not null,
    ativo tinyint not null,

    primary key (id)
);