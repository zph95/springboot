CREATE TABLE Rest_Call_Log_Record (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	user_id INTEGER,
	method TEXT NOT NULL,
	uri TEXT NOT NULL,
	request TEXT,
	response TEXT,
	status INTEGER DEFAULT 0 NOT NULL,
	cost_time INTEGER,
	is_valid INTEGER DEFAULT 1 NOT NULL,
	created_time TIMESTAMP default (datetime('now', 'localtime')),
	modified_time TIMESTAMP default (datetime('now', 'localtime'))
);

CREATE TRIGGER Rest_Call_Log_Record_Update  after update on Rest_Call_Log_Record
BEGIN
	update Rest_Call_Log_Record set modified_time =datetime('now', 'localtime') where id=old.id;
END