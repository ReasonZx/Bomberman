CREATE TABLE users(
    id  SERIAL PRIMARY KEY,
	username VARCHAR UNIQUE, 
	password VARCHAR NOT NULL,
	gamesPlayed INT,
	gamesWon INT
);

CREATE TABLE friends(
    user SERIAL REFERENCES users(username),
    friend SERIAL REFERENCES users(username),
    confirmed BOOLEAN
);
