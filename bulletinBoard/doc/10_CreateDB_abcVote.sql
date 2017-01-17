-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 16. Jan 2017 um 20:56
-- Server Version: 5.5.53-0+deb8u1
-- PHP-Version: 5.6.29-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `abcVote`
--
CREATE DATABASE IF NOT EXISTS `abcVote` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `abcVote`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_ballots`
--

DROP TABLE IF EXISTS `tbl_ballots`;
CREATE TABLE IF NOT EXISTS `tbl_ballots` (
`id` int(11) NOT NULL,
  `electionIdentifier` int(11) NOT NULL,
  `jsonData` mediumtext NOT NULL,
  `ballotTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ipAddress` varchar(64) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_certificates`
--

DROP TABLE IF EXISTS `tbl_certificates`;
CREATE TABLE IF NOT EXISTS `tbl_certificates` (
  `email` varchar(64) NOT NULL,
  `certificate` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_elections`
--

DROP TABLE IF EXISTS `tbl_elections`;
CREATE TABLE IF NOT EXISTS `tbl_elections` (
`id` int(11) NOT NULL,
  `jsonData` text NOT NULL,
  `electionTitle` varchar(512) NOT NULL,
  `beginDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  `coefficients` text NOT NULL,
  `appVersion` varchar(32) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_parameters`
--

DROP TABLE IF EXISTS `tbl_parameters`;
CREATE TABLE IF NOT EXISTS `tbl_parameters` (
  `parameterName` varchar(32) NOT NULL,
  `parameterValue` varchar(2048) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_results`
--

DROP TABLE IF EXISTS `tbl_results`;
CREATE TABLE IF NOT EXISTS `tbl_results` (
`id` int(11) NOT NULL,
  `electionIdentifier` int(11) NOT NULL,
  `jsonData` mediumtext NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_voters`
--

DROP TABLE IF EXISTS `tbl_voters`;
CREATE TABLE IF NOT EXISTS `tbl_voters` (
`id` int(11) NOT NULL,
  `jsonData` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `tbl_ballots`
--
ALTER TABLE `tbl_ballots`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_ballot-electionIdentifier` (`electionIdentifier`);

--
-- Indizes für die Tabelle `tbl_certificates`
--
ALTER TABLE `tbl_certificates`
 ADD PRIMARY KEY (`email`);

--
-- Indizes für die Tabelle `tbl_elections`
--
ALTER TABLE `tbl_elections`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `tbl_parameters`
--
ALTER TABLE `tbl_parameters`
 ADD PRIMARY KEY (`parameterName`);

--
-- Indizes für die Tabelle `tbl_results`
--
ALTER TABLE `tbl_results`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_result-electionIdentifier` (`electionIdentifier`);

--
-- Indizes für die Tabelle `tbl_voters`
--
ALTER TABLE `tbl_voters`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `tbl_ballots`
--
ALTER TABLE `tbl_ballots`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=102;
--
-- AUTO_INCREMENT für Tabelle `tbl_elections`
--
ALTER TABLE `tbl_elections`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=55;
--
-- AUTO_INCREMENT für Tabelle `tbl_results`
--
ALTER TABLE `tbl_results`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT für Tabelle `tbl_voters`
--
ALTER TABLE `tbl_voters`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=39;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `tbl_ballots`
--
ALTER TABLE `tbl_ballots`
ADD CONSTRAINT `FK_ballot-electionIdentifier` FOREIGN KEY (`electionIdentifier`) REFERENCES `tbl_elections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `tbl_results`
--
ALTER TABLE `tbl_results`
ADD CONSTRAINT `FK_result-electionIdentifier` FOREIGN KEY (`electionIdentifier`) REFERENCES `tbl_elections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
