create table transacoes(
    id bigint not null auto_increment,
    remetente_id bigint not null,
    destinatario_id bigint not null,
    valor decimal(10,2) not null,
    data datetime not null,
    ativo tinyint not null,

    primary key(id),
    constraint remetente_fk foreign key(remetente_id) references usuarios(id),
    constraint destinatario_fk foreign key(destinatario_id) references usuarios(id)
);