
CREATE TABLE refresh_token_tb (
                                  id UUID PRIMARY KEY NOT NULL,
                                  user_id UUID NOT NULL,
                                  family_id UUID NOT NULL,
                                  token_hash CHAR(64) NOT NULL UNIQUE,
                                  create_at TIMESTAMP NOT NULL,
                                  expired_at TIMESTAMP NOT NULL,
                                  family_expired_at TIMESTAMP NOT NULL,
                                  consumed_at TIMESTAMP,
                                  revoke_at TIMESTAMP,
                                  replace_by_id UUID,

                                  CONSTRAINT fk_refresh_token_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users(id),

                                  CONSTRAINT fk_refresh_token_replaced_by
                                      FOREIGN KEY (replace_by_id)
                                          REFERENCES refresh_token_tb(id)
);

CREATE INDEX idx_refresh_tokens_family
    ON refresh_token_tb(family_id);

CREATE INDEX idx_refresh_tokens_user
    ON refresh_token_tb(user_id);
