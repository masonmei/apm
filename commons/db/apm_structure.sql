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

 Date: 08/17/2015 21:46:03 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `apm_annotation`
-- ----------------------------
DROP TABLE IF EXISTS `apm_annotation`;
CREATE TABLE `apm_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` tinyint(4) NOT NULL,
  `span_id` bigint(20) NOT NULL,
  `key` int(11) DEFAULT NULL,
  `value_type` tinyint(4) DEFAULT NULL,
  `byte_value` binary(50) DEFAULT NULL,
  `trace_id` bigint(20) DEFAULT NULL,
  `trace_event_id` bigint(20) DEFAULT NULL,
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
  `version` tinyint(4) DEFAULT '0',
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
  `flag` tinyint(4) DEFAULT NULL,
  `err_code` int(11) DEFAULT NULL,
  `collector_accept_time` bigint(20) DEFAULT NULL,
  `has_exception` bit(1) NOT NULL,
  `exception_id` int(11) DEFAULT NULL,
  `exception_message` varchar(512) DEFAULT NULL,
  `exception_class` varchar(512) DEFAULT NULL,
  `remote_addr` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apm_span_event`
-- ----------------------------
DROP TABLE IF EXISTS `apm_span_event`;
CREATE TABLE `apm_span_event` (
  `id` bit(1) NOT NULL,
  `version` tinyint(4) DEFAULT NULL,
  `agent_id` varchar(128) NOT NULL,
  `application_id` varchar(128) NOT NULL,
  `agent_start_time` bigint(20) NOT NULL,
  `trace_agent_id` varchar(128) DEFAULT NULL,
  `trace_agent_start_time` bigint(20) DEFAULT NULL,
  `trace_transaction_sequence` bigint(20) DEFAULT NULL,
  `span_id` bigint(20) DEFAULT NULL,
  `sequence` tinyint(4) DEFAULT NULL,
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
