create table article (
    article_id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    board_type varchar(255),
    client_type varchar(255),
    content TEXT not null,
    created_by varchar(255) not null,
    customer_id bigint not null,
    title varchar(255) not null,
    PRIMARY KEY (article_id)
)engine=InnoDB;

create table comment (
    comment_id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    board_type varchar(255),
    content TEXT not null,
    customer_id bigint not null,
    article_id bigint,
    primary key (comment_id),
    FOREIGN KEY (article_id) references article(article_id)
) engine=InnoDB;