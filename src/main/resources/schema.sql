-- just received messages
CREATE CACHED TABLE IF NOT EXISTS message (
  id           VARCHAR(38) PRIMARY KEY,
  date_time    TIMESTAMP,
  message_type VARCHAR(10)
);

-- just received messages data
CREATE CACHED TABLE IF NOT EXISTS message_data (
  id    VARCHAR(38),
  name  VARCHAR(20),
  value VARCHAR(255),
  CONSTRAINT pk_message_data PRIMARY KEY (id, name),
  CONSTRAINT fk_message_id FOREIGN KEY (id) REFERENCES message (id)
    ON DELETE CASCADE
);

-- prepared messages for sending
CREATE CACHED TABLE IF NOT EXISTS email_message (
  id           VARCHAR(38) PRIMARY KEY,
  date_time    TIMESTAMP,
  message_type VARCHAR(10),
  address_from VARCHAR(120),
  address_to   VARCHAR(120),
  address_copy VARCHAR(120),
  address_bcc  VARCHAR(120),
  subject      VARCHAR(255),
  text_body    VARCHAR(4000)
);

-- backup messages after successfull sending
CREATE CACHED TABLE IF NOT EXISTS backup_email_message (
  id           VARCHAR(38) PRIMARY KEY,
  date_time    TIMESTAMP,
  message_type VARCHAR(10),
  address_from VARCHAR(120),
  address_to   VARCHAR(120),
  address_copy VARCHAR(120),
  address_bcc  VARCHAR(120),
  subject      VARCHAR(255),
  text_body    VARCHAR(4000),
  sent         TIMESTAMP
);

-- backup messages data after successfull sending
CREATE CACHED TABLE IF NOT EXISTS backup_message_data (
  id     VARCHAR(38),
  name   VARCHAR(20),
  value  VARCHAR(255),
  CONSTRAINT pk_backup_message_data PRIMARY KEY (id, name)
);

