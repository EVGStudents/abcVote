-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 17. Jan 2017 um 16:41
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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tbl_certificates`
--

CREATE TABLE IF NOT EXISTS `tbl_certificates` (
  `email` varchar(64) NOT NULL,
  `certificate` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `tbl_certificates`
--

INSERT INTO `tbl_certificates` (`email`, `certificate`) VALUES
('alice@bfh.ch', '-----BEGIN CERTIFICATE-----\r\nMIIDZjCCAk6gAwIBAgIBAjANBgkqhkiG9w0BAQsFADCBkzETMBEGA1UEAwwKYWJj\r\nVm90ZV9DQTEiMCAGA1UECgwZQmVybmVyIEZhY2hob2Noc2NodWxlIEJGSDEPMA0G\r\nA1UECwwGQkZILVRJMQswCQYDVQQIDAJCRTELMAkGA1UEBhMCQ0gxDTALBgNVBAcM\r\nBEJlcm4xHjAcBgkqhkiG9w0BCQEWD2FiY3ZvdGVAMjQ4OC5jaDAeFw0xNzAxMTYw\r\nOTQ5MDFaFw0xOTAxMTYwOTQ5MDFaMDQxFTATBgNVBAMMDGFsaWNlQGJmaC5jaDEb\r\nMBkGCSqGSIb3DQEJARYMYWxpY2VAYmZoLmNoMIIBIjANBgkqhkiG9w0BAQEFAAOC\r\nAQ8AMIIBCgKCAQEAxuTyrx1VyKBnrdRV+sq6Vmct7/S8qlXN2efCXyIC1WtFF2Pl\r\nAkv2zs1t0jPr5xUSmIHJQcDOmj1Sy7qJzcT8I0FrHWBeT7ycLFOIRD0yb6ouwxs2\r\nBJSk8LPf3aCuYLZfvzZG/QcLSwHdacaZTyQxOVjjhJoHJEUk3NT+RZrPgosITuch\r\nCOdPWvpnQOUVsE0KDrFx+9fjvqVZL8r3G7b5C/Pf3hb+dBGDNiOXacylM5mzox+y\r\ntu/QctursLSRxU7v7TW+fwqXYTc4RxnVA0118ue59obRVNXRSEy8c7Y9ByF95dfP\r\nlyZ469G76shtue3H196I/chEkKesowF7M69xcQIDAQABoyMwITAOBgNVHQ8BAf8E\r\nBAMCB4AwDwYDVR0lBAgwBgYEVR0lADANBgkqhkiG9w0BAQsFAAOCAQEA3NNbGeIO\r\nuto9NtAAR1ZhyAl4r/RgED+bPHrwR7TaaX+2ZGTH/Kdq65KP0c6zM3B4NyHQt/LT\r\nwM3wtBnTNdoLhMmuhksbNy9kd1YmnVl6chHPLgVF3Qx9mVipCq1PfVjyxh22+IKv\r\nuRA+7z0hMCLpYMGCxRJxeZ6A9Sum7vhqY0mP0pW9dsB8ap5uYP6PkA+1HwS2wTtn\r\nRKKOgxwbXx4+xTMj5r1te1P1CgQC/ne8B0bZtfMaq3r+9UoVLnBwmgLPHccny1SO\r\nm+67yXS4kjYBb3+JIfFfWhNRvC8+cRSNgnGuVWcdJnGkLVLwZQP/81oTN+tJ0vs8\r\n++CtZEFn0ROrNA==\r\n-----END CERTIFICATE-----'),
('bob@bfh.ch', '-----BEGIN CERTIFICATE-----\r\nMIIDYjCCAkqgAwIBAgIBAzANBgkqhkiG9w0BAQsFADCBkzETMBEGA1UEAwwKYWJj\r\nVm90ZV9DQTEiMCAGA1UECgwZQmVybmVyIEZhY2hob2Noc2NodWxlIEJGSDEPMA0G\r\nA1UECwwGQkZILVRJMQswCQYDVQQIDAJCRTELMAkGA1UEBhMCQ0gxDTALBgNVBAcM\r\nBEJlcm4xHjAcBgkqhkiG9w0BCQEWD2FiY3ZvdGVAMjQ4OC5jaDAeFw0xNzAxMTYw\r\nOTQ5NDZaFw0xOTAxMTYwOTQ5NDZaMDAxEzARBgNVBAMMCmJvYkBiZmguY2gxGTAX\r\nBgkqhkiG9w0BCQEWCmJvYkBiZmguY2gwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\r\nggEKAoIBAQDXqkvfuOBCVwvTIuvIHslf4vUUFyVp20ttrgM6SW/8Row5HpSMattJ\r\nY11kZCS/kFof0v4RfpKtOGO4a6mVUSFJItxXOHvsXuMC3Bbc9/4IXEtMKY0ocEP4\r\n+RQwpMclpCb8jDgn+NrrI0AXbHdjIShAoL50x8LIS4Iz7IStUL7gnW9T/l8Afq1X\r\naNHU34b+P5PhHL5yE1ggZzKr6rhiD6Xafi2ZzrzlwUAUUMwxJdQ8vSeUXUVe+5n7\r\nHOosA9pk1I1STUAHY02FiQlsupG4fDC+CJ0hKCWNgy2MjY+TsB+vz6dERWytenXR\r\nz2CHIyLM79z28IIEGV3cKOXJY3x1G2k9AgMBAAGjIzAhMA4GA1UdDwEB/wQEAwIH\r\ngDAPBgNVHSUECDAGBgRVHSUAMA0GCSqGSIb3DQEBCwUAA4IBAQDelLY986RD20fI\r\nca5axA7ozgvRXvNBQJaYJ1ynEMjzXGB4y7TbUeiSRnA6zNTNfH1uoCkMBc4EFo5X\r\nRkMWiUSML21naF1BJtDsNwN0M053QH6a+/xaAooZQSqjh9KeSM2rDbhxLJ6S+a2m\r\nSrHCZdgVnzBaiqHpodqh2jSkcBhqChwfK5+/4s03wJz9pRL2QzwxKmZkJKR2YuuR\r\n70iKtZ6g7qD8/PE+ud0C3TDOnOtsb6oHtDCuL5XFwP4SGrn7Ri2FheILHXcpekyT\r\n14SXD4snCvVWfalQQhJN+E2Ny+oZ40QgKRU2BRboq05LYF9LlbHur6bFwya71Nkd\r\nsOVFbJ98\r\n-----END CERTIFICATE-----'),
('caesar@bfh.ch', '-----BEGIN CERTIFICATE-----\r\nMIIDaDCCAlCgAwIBAgIBBDANBgkqhkiG9w0BAQsFADCBkzETMBEGA1UEAwwKYWJj\r\nVm90ZV9DQTEiMCAGA1UECgwZQmVybmVyIEZhY2hob2Noc2NodWxlIEJGSDEPMA0G\r\nA1UECwwGQkZILVRJMQswCQYDVQQIDAJCRTELMAkGA1UEBhMCQ0gxDTALBgNVBAcM\r\nBEJlcm4xHjAcBgkqhkiG9w0BCQEWD2FiY3ZvdGVAMjQ4OC5jaDAeFw0xNzAxMTYw\r\nOTUyNTNaFw0xOTAxMTYwOTUyNTNaMDYxFjAUBgNVBAMMDWNhZXNhckBiZmguY2gx\r\nHDAaBgkqhkiG9w0BCQEWDWNhZXNhckBiZmguY2gwggEiMA0GCSqGSIb3DQEBAQUA\r\nA4IBDwAwggEKAoIBAQDKhyfHQVtVkZw486Z/HWLjlX+rf1HOXtzTfYykI12zXADH\r\n0kg99vpwMNd+fopGdBuVdvnWQy2LN46aSMv7qp/dt0Dgr/gwukfI0esMBB0nU0Su\r\nZvUsFj61JKC4OeZsMp3auzO7SLYbhDfr/4gHO7A63BskNGtO80KOfO4sbxfDGy4S\r\naVCee/BgcHohY832E3MoeIz3gojIkn+L14BqXh4mcsa+I1uZeCoTM2c1pA0R1uhY\r\nATq/x/YpRvPkyGtnCPI0Bhr/LsMZ6moh3k/olklgn2a9W4sABw2rb6mlePQBCCtU\r\n8J/Q7LrJPjD8z0bkqLP8uUfKeJSq5uWEvxI1t1a5AgMBAAGjIzAhMA4GA1UdDwEB\r\n/wQEAwIHgDAPBgNVHSUECDAGBgRVHSUAMA0GCSqGSIb3DQEBCwUAA4IBAQB3V58t\r\nnvUmthWs6B0G4gMkMVehndQ9jU6sYF+smhOZcMp9uQ0REA0WWLVepJHL9SWl84xJ\r\nR/51MrytB/B0RP7P/pmfLockqDFY7Sl50FZYLh0D6bq2N9g8hGlQnqzSBIo1fpJ9\r\nM0ccRX7c7TdcIEVLwOIRIRGhMOe3VZy5G/eQggw/kPmwc77rQ7HEXiHP/hoMCYLS\r\nkP3Ar2xhF6QkcXGEr4hNyw82PlDe7pygwwJhtmywIlURYnXqMYC7J1KevXXNjY4F\r\n8rYPqNb032eVIG54Y87D3azbJzKkAuMweu9QypQQU3xWLGZe7fsDo+scaDZzwyYs\r\no+6a6LSWEYHEkOBs\r\n-----END CERTIFICATE-----'),
('dave@bfh.ch', '-----BEGIN CERTIFICATE-----\r\nMIIDZDCCAkygAwIBAgIBBTANBgkqhkiG9w0BAQsFADCBkzETMBEGA1UEAwwKYWJj\r\nVm90ZV9DQTEiMCAGA1UECgwZQmVybmVyIEZhY2hob2Noc2NodWxlIEJGSDEPMA0G\r\nA1UECwwGQkZILVRJMQswCQYDVQQIDAJCRTELMAkGA1UEBhMCQ0gxDTALBgNVBAcM\r\nBEJlcm4xHjAcBgkqhkiG9w0BCQEWD2FiY3ZvdGVAMjQ4OC5jaDAeFw0xNzAxMTYw\r\nOTUzMjlaFw0xOTAxMTYwOTUzMjlaMDIxFDASBgNVBAMMC2RhdmVAYmZoLmNoMRow\r\nGAYJKoZIhvcNAQkBFgtkYXZlQGJmaC5jaDCCASIwDQYJKoZIhvcNAQEBBQADggEP\r\nADCCAQoCggEBAKoLw1EN1sZ267RQCX1Zjcv/VwUzjwh1rO/hjGDlZ8Z3xVmsLHr1\r\nWnkkk41DFhWSXTj5dDYVeHKZU/qSti1Y7nncrGUXyjwSTcnEr63rkSKHip1cnbBt\r\nwMrX2r6GSCAhYn8GurtMtdifnEdHbIuStjl4B5EQdUTol+qRCuAb8YjDm81fBvG8\r\nFngwJ2OF0KFTy+lmgBOIl7xb+/i1GCcVxhldsGkMnGHJDPvDvz9nuHmSLP+qDblj\r\nrpbbwJIsf5impbixpNU6J53jRj4x9n+ppMjaRQ/jNeMW8mu2egaxnz6ljJPMoF4c\r\n02yh7CWF6GTMw5aV913JBmcH5n2RhiqRMl0CAwEAAaMjMCEwDgYDVR0PAQH/BAQD\r\nAgeAMA8GA1UdJQQIMAYGBFUdJQAwDQYJKoZIhvcNAQELBQADggEBANoNDdaoi47y\r\nBFWZV3pUphydqVkIYTJYyot5KwgE1Gl5+iLuVG7EDvrRIWUpoK7N0/EMbV72rDxY\r\nVjiRrJcmkUS9V+FZaM/EipHNLmHihKhfH7Y1dhpXHRjfv28gongxPmMh0T4bkF7q\r\nUOL6gj+Q/ueW+WJg32vuZwzcznYq60264TdhPYbFaCFR02tioUeLtBNLIGQCrfyU\r\naXpaM/0AbsG3FKvLFqvuppjH9+PCTW6tIcQXFnOZ51C5JERDK7Jl3n6+8SgvYWvf\r\ncRMCLsEA/pgqz9+fJ7m+eHExVea0mzEjAWQubViYTRbjpea9JsvdLNpp9crNPVlV\r\nmDlEmSwO4wg=\r\n-----END CERTIFICATE-----'),
('eve@bfh.ch', '-----BEGIN CERTIFICATE-----\r\nMIIDYjCCAkqgAwIBAgIBBjANBgkqhkiG9w0BAQsFADCBkzETMBEGA1UEAwwKYWJj\r\nVm90ZV9DQTEiMCAGA1UECgwZQmVybmVyIEZhY2hob2Noc2NodWxlIEJGSDEPMA0G\r\nA1UECwwGQkZILVRJMQswCQYDVQQIDAJCRTELMAkGA1UEBhMCQ0gxDTALBgNVBAcM\r\nBEJlcm4xHjAcBgkqhkiG9w0BCQEWD2FiY3ZvdGVAMjQ4OC5jaDAeFw0xNzAxMTYw\r\nOTUzNTlaFw0xOTAxMTYwOTUzNTlaMDAxEzARBgNVBAMMCmV2ZUBiZmguY2gxGTAX\r\nBgkqhkiG9w0BCQEWCmV2ZUBiZmguY2gwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\r\nggEKAoIBAQCVWS1cvelA7CE8HXKQ45z4CfaThJVGEwaCk36gqnKg4XJTgsA81nhL\r\nlK4dDSZX9yp11NShuX7M+zgpHhogWaYT450tk/NquzxrdMdwJOxu0GQXFXDjHEMt\r\nWaQVF4qC6j6Q7DWibAtl3TuH1PxhMLWSrp05IFGbMpb/x6i+YIFhQTYgnZKyNo0B\r\nT7jzdeo2IzswfSwPjA2vd3Xsts6ERRc7/f1oZENx3gbX7PBSM8gVHN8LMrG9ogMk\r\nUWzcXamui0LfIBiJ1rus7EBHJj5oZxqiX93+nFU02Bf1fjwF6tyKfpp49uTre4jM\r\nYNEn7nAdQy8m7Kn0pI0o3pza0K26h4qzAgMBAAGjIzAhMA4GA1UdDwEB/wQEAwIH\r\ngDAPBgNVHSUECDAGBgRVHSUAMA0GCSqGSIb3DQEBCwUAA4IBAQBTkL0x2j1FsUc+\r\nbA9q5T9jdgG0P7luZTVM7806Ve/6Rp8x6tJeL7JbuyTJR1gdKMdLAiVsI7JnpGp1\r\n4qBVmP3i3X5p+v5rD1E+CB4G17jZL9GF41tuXq/lGCMwjCczWiRmbXA0dTgMoput\r\nsmxKS+iOv2yvAR6D8b8n46Y0CHCwgXbiZK/c8u221DR/nd7WLH9KEt3fq+6pyeR4\r\nwuCeszvcblIZZibCAk/ErERCCVj1A3m/EK55jD/xMT6PaXz7FbQ05WltQxz207NQ\r\nV8+lSN4zpRQQC5KShIPL2TLn25aKNqnK4Xgla2nOrTd6mDd4UV0pI3pos38Jtv2C\r\nPYiSY9Zm\r\n-----END CERTIFICATE-----');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `tbl_certificates`
--
ALTER TABLE `tbl_certificates`
 ADD PRIMARY KEY (`email`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
