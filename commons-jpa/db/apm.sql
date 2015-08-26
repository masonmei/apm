/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : localhost
 Source Database       : apm

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : utf-8

 Date: 08/26/2015 22:00:13 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `apm_agent_stat_cpu_load`
-- ----------------------------
DROP TABLE IF EXISTS `apm_agent_stat_cpu_load`;
CREATE TABLE `apm_agent_stat_cpu_load` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `jvm_cpu_load` double NOT NULL,
  `start_timestamp` bigint(20) NOT NULL,
  `system_cpu_load` double NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2407 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_agent_stat_memory_gc`
-- ----------------------------
DROP TABLE IF EXISTS `apm_agent_stat_memory_gc`;
CREATE TABLE `apm_agent_stat_memory_gc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `gc_type` varchar(255) COLLATE utf8_bin NOT NULL,
  `jvm_gc_old_count` bigint(20) NOT NULL,
  `jvm_gc_old_time` bigint(20) NOT NULL,
  `jvm_memory_heap_max` bigint(20) NOT NULL,
  `jvm_memory_heap_used` bigint(20) NOT NULL,
  `jvm_memory_non_heap_max` bigint(20) NOT NULL,
  `jvm_memory_non_heap_used` bigint(20) NOT NULL,
  `start_timestamp` bigint(20) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2407 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_annotation`
-- ----------------------------
DROP TABLE IF EXISTS `apm_annotation`;
CREATE TABLE `apm_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `byte_value` longblob,
  `key` int(11) DEFAULT NULL,
  `value_type` tinyint(4) DEFAULT NULL,
  `trace_id` bigint(20) DEFAULT NULL,
  `trace_event_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_logsqfijs01kvhsve0bxcmii4` (`trace_id`),
  KEY `FK_qbeugbp91axjv4i0y51ia24r3` (`trace_event_id`),
  CONSTRAINT `FK_logsqfijs01kvhsve0bxcmii4` FOREIGN KEY (`trace_id`) REFERENCES `apm_trace` (`id`),
  CONSTRAINT `FK_qbeugbp91axjv4i0y51ia24r3` FOREIGN KEY (`trace_event_id`) REFERENCES `apm_trace_event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_api_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_api_meta_data`;
CREATE TABLE `apm_api_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `api_id` int(11) NOT NULL,
  `api_info` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  `start_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_application`
-- ----------------------------
DROP TABLE IF EXISTS `apm_application`;
CREATE TABLE `apm_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) NOT NULL,
  `app_type` varchar(64) DEFAULT NULL,
  `user_id` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_application_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `apm_application_statistic`;
CREATE TABLE `apm_application_statistic` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `response_time` decimal(20,4) DEFAULT NULL,
  `cpm` decimal(10,0) DEFAULT NULL,
  `error_rate` decimal(10,0) DEFAULT NULL,
  `apdex` decimal(10,0) DEFAULT NULL,
  `satisfied` bigint(20) DEFAULT NULL,
  `tolerated` bigint(20) DEFAULT NULL,
  `frustrated` bigint(20) DEFAULT NULL,
  `max_response_time` decimal(10,0) DEFAULT NULL,
  `min_response_time` decimal(10,0) DEFAULT NULL,
  `cpu_usage` decimal(32,4) DEFAULT NULL,
  `memory_usage` decimal(32,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  CONSTRAINT `apm_application_statistic_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `apm_application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_external_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `apm_external_transaction`;
CREATE TABLE `apm_external_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  `external_rpc` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_external_transaction_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `apm_external_transaction_statistic`;
CREATE TABLE `apm_external_transaction_statistic` (
  `id` bigint(20) NOT NULL,
  `external_transaction_id` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `response_time` decimal(20,4) DEFAULT NULL,
  `cpm` decimal(10,0) DEFAULT NULL,
  `error_rate` decimal(10,0) DEFAULT NULL,
  `apdex` decimal(10,0) DEFAULT NULL,
  `satisfied` bigint(20) DEFAULT NULL,
  `tolerated` bigint(20) DEFAULT NULL,
  `frustrated` bigint(20) DEFAULT NULL,
  `max_response_time` decimal(10,0) DEFAULT NULL,
  `min_response_time` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `external_transaction_id` (`external_transaction_id`),
  CONSTRAINT `apm_external_transaction_statistic_ibfk_1` FOREIGN KEY (`external_transaction_id`) REFERENCES `apm_external_transaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_instance`
-- ----------------------------
DROP TABLE IF EXISTS `apm_instance`;
CREATE TABLE `apm_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `port` int(11) NOT NULL,
  `host` varchar(128) NOT NULL,
  `ip` varchar(128) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `start_time` bigint(20) NOT NULL,
  `instance_type` int(11) NOT NULL,
  `args` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_instance_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `apm_instance_statistic`;
CREATE TABLE `apm_instance_statistic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `instance_id` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `response_time` decimal(20,4) DEFAULT NULL,
  `cpm` decimal(10,0) DEFAULT NULL,
  `error_rate` decimal(10,0) DEFAULT NULL,
  `apdex` decimal(10,0) DEFAULT NULL,
  `satisfied` bigint(20) DEFAULT NULL,
  `tolerated` bigint(20) DEFAULT NULL,
  `frustrated` bigint(20) DEFAULT NULL,
  `max_response_time` decimal(10,0) DEFAULT NULL,
  `min_response_time` decimal(10,0) DEFAULT NULL,
  `cpu_usage` decimal(32,4) DEFAULT NULL,
  `memory_usage` decimal(32,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `instance_id` (`instance_id`),
  CONSTRAINT `apm_instance_statistic_ibfk_1` FOREIGN KEY (`instance_id`) REFERENCES `apm_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_sql_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_sql_meta_data`;
CREATE TABLE `apm_sql_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `hash_code` int(11) NOT NULL,
  `sql` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `start_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_sql_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `apm_sql_transaction`;
CREATE TABLE `apm_sql_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  `sql_id` bigint(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_sql_transaction_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `apm_sql_transaction_statistic`;
CREATE TABLE `apm_sql_transaction_statistic` (
  `id` bigint(20) NOT NULL,
  `sql_transaction_id` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `response_time` decimal(20,4) DEFAULT NULL,
  `cpm` decimal(10,0) DEFAULT NULL,
  `error_rate` decimal(10,0) DEFAULT NULL,
  `apdex` decimal(10,0) DEFAULT NULL,
  `satisfied` bigint(20) DEFAULT NULL,
  `tolerated` bigint(20) DEFAULT NULL,
  `frustrated` bigint(20) DEFAULT NULL,
  `max_response_time` decimal(10,0) DEFAULT NULL,
  `min_response_time` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sql_transaction_id` (`sql_transaction_id`),
  CONSTRAINT `apm_sql_transaction_statistic_ibfk_1` FOREIGN KEY (`sql_transaction_id`) REFERENCES `apm_sql_transaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_string_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_string_meta_data`;
CREATE TABLE `apm_string_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `string_id` int(11) NOT NULL,
  `string_value` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_trace`
-- ----------------------------
DROP TABLE IF EXISTS `apm_trace`;
CREATE TABLE `apm_trace` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `agent_start_time` bigint(20) NOT NULL,
  `api_id` int(11) DEFAULT NULL,
  `application_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `collector_accept_time` bigint(20) DEFAULT NULL,
  `elapsed` int(11) NOT NULL,
  `end_point` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `err_code` int(11) DEFAULT NULL,
  `exception_class` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `exception_id` int(11) DEFAULT NULL,
  `exception_message` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `flag` smallint(6) NOT NULL,
  `has_exception` bit(1) NOT NULL,
  `parent_trace_id` bigint(20) DEFAULT NULL,
  `remote_addr` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `rpc` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `service_type` int(11) DEFAULT NULL,
  `trace_id` bigint(20) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `trace_agent_id` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `trace_agent_start_time` bigint(20) DEFAULT NULL,
  `trace_transaction_sequence` bigint(20) DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_trace_event`
-- ----------------------------
DROP TABLE IF EXISTS `apm_trace_event`;
CREATE TABLE `apm_trace_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `agent_start_time` bigint(20) NOT NULL,
  `api_id` int(11) DEFAULT NULL,
  `application_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `collector_accept_time` bigint(20) DEFAULT NULL,
  `depth` int(11) NOT NULL,
  `destination_id` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `end_elapsed` int(11) DEFAULT NULL,
  `end_point` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `exception_class` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `exception_id` int(11) DEFAULT NULL,
  `exception_message` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `has_exception` bit(1) DEFAULT NULL,
  `next_trace_id` bigint(20) NOT NULL,
  `rpc` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `sequence` smallint(6) NOT NULL,
  `service_type` int(11) DEFAULT NULL,
  `trace_id` bigint(20) DEFAULT NULL,
  `start_elapsed` int(11) DEFAULT NULL,
  `trace_agent_id` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `trace_agent_start_time` bigint(20) DEFAULT NULL,
  `trace_transaction_sequence` bigint(20) DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jbvmb3968a88icpwybs2d0ctl` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `apm_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `apm_transaction`;
CREATE TABLE `apm_transaction` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  `api_id` bigint(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_transaction_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `apm_transaction_statistic`;
CREATE TABLE `apm_transaction_statistic` (
  `id` bigint(20) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `response_time` decimal(20,4) DEFAULT NULL,
  `cpm` decimal(10,0) DEFAULT NULL,
  `error_rate` decimal(10,0) DEFAULT NULL,
  `apdex` decimal(10,0) DEFAULT NULL,
  `satisfied` bigint(20) DEFAULT NULL,
  `tolerated` bigint(20) DEFAULT NULL,
  `frustrated` bigint(20) DEFAULT NULL,
  `max_response_time` decimal(10,0) DEFAULT NULL,
  `min_response_time` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `transaction_id` (`transaction_id`),
  CONSTRAINT `apm_transaction_statistic_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `apm_transaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
