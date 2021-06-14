CREATE TABLE Rest_Call_Log_Record (
	id  int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),
	method  varchar(50)NOT NULL,
	uri  varchar(50) NOT NULL,
	request  varchar(50),
	response varchar(50),
	status int(11) DEFAULT 0 NOT NULL,
	cost_time int(11),
	is_valid int(11) DEFAULT 1 NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);
