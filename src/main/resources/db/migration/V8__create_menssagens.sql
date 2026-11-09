CREATE TABLE menssagens_tb(
    id UUID PRIMARY KEY,
    conteudo TEXT NOT NULL,
    data_envio TIMESTAMP WITH TIME ZONE NOT NULL,

    mentoria_id BIGINT NOT NULL,
    user_id UUID NOT NULL,

    CONSTRAINT fk_mensagem_mentoria
        FOREIGN KEY (mentoria_id)
            REFERENCES mentoria_tb(id),

    CONSTRAINT fk_mensagem_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
);