--tabela da Mentoria

CREATE TABLE mentoria_tb(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mentor_id UUID NOT NULL,
    titulo VARCHAR(250) NOT NULL,
    status VARCHAR(30) NOT NULL
        CHECK (status IN ('ABERTA', 'EM_ANDAMENTO', 'ENCERRADA')),
    quantidade INTEGER NOT NULL CHECK (quantidade BETWEEN 1 AND 15),
    data_encerramento DATE NOT NULL,
    descricao VARCHAR(1500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_mentoria_user
                        FOREIGN KEY (mentor_id)
                        REFERENCES users(id)

);