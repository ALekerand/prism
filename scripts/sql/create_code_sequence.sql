-- Table de séquence utilisée par la génération de codes (PREFIX + 000001...)
-- Compatible MySQL / MariaDB

CREATE TABLE IF NOT EXISTS code_sequence (
  PREFIX VARCHAR(10) NOT NULL,
  NEXT_VALUE BIGINT NOT NULL,
  PRIMARY KEY (PREFIX)
);

