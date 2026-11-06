CREATE TABLE inscricao_tb(

    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mentorado_id UUID NOT NULL,
    mentoria_id BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_inscricao_user
        FOREIGN KEY (mentorado_id)
            REFERENCES users(id),

    CONSTRAINT fk_inscricao_mentoria
        FOREIGN KEY (mentoria_id)
            REFERENCES mentoria_tb(id)
);