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

 Date: 08/20/2015 16:09:39 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `apm_agent_info`
-- ----------------------------
DROP TABLE IF EXISTS `apm_agent_info`;
CREATE TABLE `apm_agent_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host_name` varchar(255) NOT NULL DEFAULT '' COMMENT '机器名字',
  `ip` varchar(255) NOT NULL DEFAULT '',
  `ports` varchar(512) NOT NULL DEFAULT '',
  `agent_id` varchar(128) NOT NULL DEFAULT '',
  `application_name` varchar(255) NOT NULL DEFAULT '',
  `service_type` varchar(255) NOT NULL DEFAULT '',
  `pid` int(11) NOT NULL DEFAULT '0',
  `version` varchar(255) NOT NULL DEFAULT '',
  `start_time` bigint(20) NOT NULL DEFAULT '0',
  `end_time_stamp` bigint(20) NOT NULL DEFAULT '0',
  `end_status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_agent_stat_cpu_load`
-- ----------------------------
DROP TABLE IF EXISTS `apm_agent_stat_cpu_load`;
CREATE TABLE `apm_agent_stat_cpu_load` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL DEFAULT '',
  `start_timestamp` bigint(20) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0',
  `jvm_cpu_load` double NOT NULL DEFAULT '0',
  `system_cpu_load` double unsigned zerofill NOT NULL DEFAULT '0000000000000000000000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_agent_stat_memory_gc`
-- ----------------------------
DROP TABLE IF EXISTS `apm_agent_stat_memory_gc`;
CREATE TABLE `apm_agent_stat_memory_gc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL DEFAULT '',
  `start_timestamp` bigint(20) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0',
  `gc_type` varchar(255) NOT NULL DEFAULT '',
  `jvm_memory_heap_used` bigint(20) NOT NULL DEFAULT '0',
  `jvm_memory_heap_max` bigint(20) NOT NULL DEFAULT '0',
  `jvm_memory_non_heap_used` bigint(20) NOT NULL DEFAULT '0',
  `jvm_memory_non_heap_max` bigint(20) NOT NULL DEFAULT '0',
  `jvm_gc_old_count` bigint(20) NOT NULL DEFAULT '0',
  `jvm_gc_old_time` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_annotation`
-- ----------------------------
DROP TABLE IF EXISTS `apm_annotation`;
CREATE TABLE `apm_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `span_id` bigint(20) NOT NULL,
  `key` int(11) DEFAULT NULL,
  `value_type` int(11) DEFAULT NULL,
  `byte_value` tinyblob,
  `trace_id` bigint(20) DEFAULT NULL,
  `trace_event_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_api_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_api_meta_data`;
CREATE TABLE `apm_api_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `api_id` int(11) NOT NULL,
  `api_info` varchar(512) DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_application_index`
-- ----------------------------
DROP TABLE IF EXISTS `apm_application_index`;
CREATE TABLE `apm_application_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_name` varchar(128) NOT NULL,
  `agent_id` varchar(128) NOT NULL,
  `service_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_host_application_map`
-- ----------------------------
DROP TABLE IF EXISTS `apm_host_application_map`;
CREATE TABLE `apm_host_application_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host` varchar(128) DEFAULT NULL,
  `bind_application_name` varchar(128) DEFAULT NULL,
  `bind_service_type` int(11) DEFAULT NULL,
  `statistics_row_slot` bigint(20) DEFAULT NULL,
  `parent_application_name` varchar(128) DEFAULT NULL,
  `parent_service_type` int(11) DEFAULT NULL,
  `parent_agent_id` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_server_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_server_meta_data`;
CREATE TABLE `apm_server_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_info` varchar(512) NOT NULL DEFAULT '',
  `vm_args` varchar(1024) NOT NULL DEFAULT '',
  `agent_info_id` bigint(20) NOT NULL,
  `agent_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_service_info`
-- ----------------------------
DROP TABLE IF EXISTS `apm_service_info`;
CREATE TABLE `apm_service_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(255) NOT NULL DEFAULT '',
  `service_libs` varchar(1024) NOT NULL DEFAULT '',
  `server_meta_data_id` bigint(20) NOT NULL,
  `agent_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_span`
-- ----------------------------
DROP TABLE IF EXISTS `apm_span`;
CREATE TABLE `apm_span` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL,
  `application_id` varchar(128) NOT NULL,
  `agent_start_time` bigint(20) NOT NULL,
  `version` int(11) DEFAULT '0',
  `trace_agent_id` varchar(128) DEFAULT NULL,
  `trace_agent_start_time` bigint(20) DEFAULT NULL,
  `trace_transaction_sequence` bigint(20) DEFAULT NULL,
  `span_id` bigint(20) DEFAULT NULL,
  `parent_span_id` bigint(20) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `elapsed` int(11) DEFAULT NULL,
  `rpc` varchar(512) DEFAULT NULL,
  `service_type` varchar(128) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `err_code` int(11) DEFAULT NULL,
  `collector_accept_time` bigint(20) DEFAULT NULL,
  `has_exception` bit(1) NOT NULL,
  `exception_id` int(11) DEFAULT NULL,
  `exception_message` varchar(512) DEFAULT NULL,
  `exception_class` varchar(512) DEFAULT NULL,
  `remote_addr` varchar(512) DEFAULT NULL,
  `end_point` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_span_event`
-- ----------------------------
DROP TABLE IF EXISTS `apm_span_event`;
CREATE TABLE `apm_span_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `agent_id` varchar(128) NOT NULL,
  `application_id` varchar(128) NOT NULL,
  `agent_start_time` bigint(20) NOT NULL,
  `trace_agent_id` varchar(128) DEFAULT NULL,
  `trace_agent_start_time` bigint(20) DEFAULT NULL,
  `trace_transaction_sequence` bigint(20) DEFAULT NULL,
  `span_id` bigint(20) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `start_elapsed` int(11) DEFAULT NULL,
  `end_elapsed` int(11) DEFAULT NULL,
  `rpc` varchar(512) DEFAULT NULL,
  `service_type` varchar(128) DEFAULT NULL,
  `destination_id` varchar(512) DEFAULT NULL,
  `end_point` varchar(512) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `depth` int(11) NOT NULL DEFAULT '-1',
  `next_span_id` bigint(20) NOT NULL DEFAULT '-1',
  `has_exception` bit(1) DEFAULT NULL,
  `exception_id` int(11) DEFAULT NULL,
  `exception_message` varchar(512) DEFAULT NULL,
  `exception_class` varchar(512) DEFAULT NULL,
  `trace_id` bigint(20) DEFAULT NULL,
  `collector_accept_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_sql_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_sql_meta_data`;
CREATE TABLE `apm_sql_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `hash_code` int(11) NOT NULL,
  `sql` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_string_meta_data`
-- ----------------------------
DROP TABLE IF EXISTS `apm_string_meta_data`;
CREATE TABLE `apm_string_meta_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(128) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `string_value` varchar(512) DEFAULT NULL,
  `string_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
