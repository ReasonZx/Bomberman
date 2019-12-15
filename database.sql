CREATE TABLE users(
    id  SERIAL PRIMARY KEY,
	username VARCHAR UNIQUE, 
	password VARCHAR NOT NULL
);

CREATE TABLE friends(
    user_id SERIAL REFERENCES users(id),
    friend_id SERIAL REFERENCES users(id),
    confirmed BOOLEAN
);
