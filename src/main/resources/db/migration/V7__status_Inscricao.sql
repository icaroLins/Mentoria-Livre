ALTER TABLE inscricao_tb ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ATIVA';

ALTER TABLE inscricao_tb
    ADD CONSTRAINT chk_status_inscricao
        CHECK (status IN ('ATIVA', 'CANCELADA'));