
-- =====================================================
-- V1 - Criação das tabelas de usuários
-- Entidades: User, Mentor e Mentorado
-- Estratégia de herança: JOINED
-- =====================================================

-- =====================================================
-- Tabela principal: users
-- =====================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       senha VARCHAR(255) NOT NULL,
                       data_nascimento DATE NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

-- =====================================================
-- Tabela: mentor_tb
-- Entidade: Mentor
-- =====================================================

CREATE TABLE mentor_tb (
                           id UUID PRIMARY KEY,
                           especialidade VARCHAR(255),

                           CONSTRAINT fk_mentor_user
                               FOREIGN KEY (id)
                                   REFERENCES users(id)
);

-- =====================================================
-- Tabela: mentorado_tb
-- Entidade: Mentorado
-- =====================================================

CREATE TABLE mentorado_tb (
                              id UUID PRIMARY KEY,
                              objetivo VARCHAR(255),

                              CONSTRAINT fk_mentorado_user
                                  FOREIGN KEY (id)
                                      REFERENCES users(id)
);
