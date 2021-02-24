DROP TABLE IF EXISTS `rest_call_log_record`;
CREATE TABLE rest_call_log_record (
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
CREATE TRIGGER rest_call_log_record_update  after update on rest_call_log_record
BEGIN
	update rest_call_log_record set modified_time =datetime('now', 'localtime') where id=old.id;
END

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
   id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
   user_uuid TEXT,
   user_name TEXT,
   real_name TEXT,
  `user_password` TEXT,
  `user_roles`TEXT,
   	is_valid INTEGER DEFAULT 1 NOT NULL,
	created_time TIMESTAMP default (datetime('now', 'localtime')),
	modified_time TIMESTAMP default (datetime('now', 'localtime'))
);
CREATE TRIGGER user_info_update  after update on user_info
BEGIN
	update user_info set modified_time =datetime('now', 'localtime') where id=old.id;
END

commit ();