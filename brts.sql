-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 10, 2024 at 10:52 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `brts`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `bookingTicket` (IN `in_userID` INT, IN `in_busID` INT, IN `in_NumberOfSeat` INT, IN `in_seatPrice` DOUBLE, IN `in_oID` INT)   BEGIN
    DECLARE t TIME;
    DECLARE id INT;
    
    SELECT DepartureTime INTO t
    FROM bus 
    WHERE busID = in_busID;

    -- Insert into booking table
    INSERT INTO booking(UserId, busID, DepartureTime)
    VALUES (in_userID, in_busID, t);

    -- Retrieve the last inserted bookingID
    SELECT LAST_INSERT_ID() INTO id;

    -- Insert into payment table
    INSERT INTO payment(bookingID, totalAmount, OperatorId, TotalSeats)
    VALUES (id, (in_NumberOfSeat * in_seatPrice), in_oID, in_NumberOfSeat);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `cancelTicket` (IN `in_bookingID` INT)   BEGIN
UPDATE `payment` SET `Status`='canceled' WHERE bookingID =in_bookingID;
DELETE From booking where bookingId = bookingID;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `myBooking` (IN `in_userId` INT)   BEGIN
SELECT BusName,p.TotalSeats,cost,TotalAmount,PaymentDate,b.BookingId
FROM booking b 
INNER JOIN payment p ON b.BookingId = p.BookingId 
INNER JOIN bus bs ON b.BusId = bs.BusId
WHERE b.UserId = in_userId;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `BookingId` int(20) NOT NULL,
  `UserId` int(20) DEFAULT NULL,
  `BusId` int(20) DEFAULT NULL,
  `DepartureDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `DepartureTime` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Triggers `booking`
--
DELIMITER $$
CREATE TRIGGER `beforeBookingDelete` BEFORE DELETE ON `booking` FOR EACH ROW insert into bookinghistory values(old.bookingid,old.userid,old.busid,old.departuredate,old.departuretime)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bookinghistory`
--

CREATE TABLE `bookinghistory` (
  `BookingHistoryId` int(20) NOT NULL,
  `UserHistoryId` int(20) DEFAULT NULL,
  `BusId` int(20) DEFAULT NULL,
  `DepartureDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `DepartureTime` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Table structure for table `bus`
--

CREATE TABLE `bus` (
  `BusId` int(20) NOT NULL,
  `BusName` varchar(128) DEFAULT NULL,
  `BusType` varchar(128) DEFAULT NULL,
  `DepartureTime` time DEFAULT NULL,
  `TravelTime` time DEFAULT NULL,
  `departureDate` date NOT NULL DEFAULT current_timestamp(),
  `DepartureCity` varchar(128) DEFAULT NULL,
  `ArrivalCity` varchar(128) DEFAULT NULL,
  `cost` double NOT NULL,
  `OperatorId` int(20) DEFAULT NULL,
  `TotalSeats` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Table structure for table `busfrequency`
--

CREATE TABLE `busfrequency` (
  `FrequencyId` int(20) NOT NULL,
  `BusId` int(20) DEFAULT NULL,
  `FrequencyTime` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `busoperator`
--

CREATE TABLE `busoperator` (
  `OperatorId` int(20) NOT NULL,
  `OperatorName` varchar(128) DEFAULT NULL,
  `Address` varchar(128) DEFAULT NULL,
  `EMail` varchar(128) DEFAULT NULL,
  `City` varchar(128) DEFAULT NULL,
  `PhoneNumber` varchar(20) DEFAULT NULL,
  `Password` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Table structure for table `operatorhistory`
--

CREATE TABLE `operatorhistory` (
  `OperatorId` int(20) NOT NULL,
  `OperatorName` varchar(128) DEFAULT NULL,
  `Address` varchar(128) DEFAULT NULL,
  `EMail` varchar(128) DEFAULT NULL,
  `City` varchar(128) DEFAULT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `ArchivedYear` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `Password` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `PaymentId` int(20) NOT NULL,
  `BookingId` int(20) DEFAULT NULL,
  `TotalAmount` int(6) DEFAULT NULL,
  `PaymentDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `OperatorId` int(20) DEFAULT NULL,
  `TotalSeats` int(3) DEFAULT NULL,
  `Status` varchar(10) DEFAULT 'PAID'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Table structure for table `seatallocation`
--

CREATE TABLE `seatallocation` (
  `SeatId` int(20) NOT NULL,
  `BookingId` int(20) DEFAULT NULL,
  `SeatType` varchar(10) DEFAULT NULL,
  `BusId` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserId` int(10) NOT NULL,
  `FirstName` varchar(64) DEFAULT NULL,
  `LastName` varchar(64) DEFAULT NULL,
  `Address` varchar(128) DEFAULT NULL,
  `City` varchar(128) DEFAULT NULL,
  `EMail` varchar(128) DEFAULT NULL,
  `PhoneNumber` varchar(50) DEFAULT NULL,
  `Password` varchar(128) DEFAULT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Triggers `user`
--
DELIMITER $$
CREATE TRIGGER `beforeUserDelete` BEFORE DELETE ON `user` FOR EACH ROW insert into userhistory values(old.userid,old.firstname,
old.lastname,old.address,old.city,old.email,old.phonenumber,default)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `userhistory`
--

CREATE TABLE `userhistory` (
  `UserHistoryId` int(20) NOT NULL,
  `FirstName` varchar(64) DEFAULT NULL,
  `LastName` varchar(64) DEFAULT NULL,
  `Address` varchar(128) DEFAULT NULL,
  `City` varchar(128) DEFAULT NULL,
  `EMail` varchar(128) DEFAULT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `ArchivedYear` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`BookingId`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `BusId` (`BusId`);

--
-- Indexes for table `bookinghistory`
--
ALTER TABLE `bookinghistory`
  ADD PRIMARY KEY (`BookingHistoryId`);

--
-- Indexes for table `bus`
--
ALTER TABLE `bus`
  ADD PRIMARY KEY (`BusId`),
  ADD KEY `OperatorId` (`OperatorId`);

--
-- Indexes for table `busfrequency`
--
ALTER TABLE `busfrequency`
  ADD PRIMARY KEY (`FrequencyId`),
  ADD KEY `BusId` (`BusId`);

--
-- Indexes for table `busoperator`
--
ALTER TABLE `busoperator`
  ADD PRIMARY KEY (`OperatorId`);

--
-- Indexes for table `operatorhistory`
--
ALTER TABLE `operatorhistory`
  ADD PRIMARY KEY (`OperatorId`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`PaymentId`),
  ADD KEY `BookingId` (`BookingId`),
  ADD KEY `OperatorId` (`OperatorId`);

--
-- Indexes for table `seatallocation`
--
ALTER TABLE `seatallocation`
  ADD PRIMARY KEY (`SeatId`),
  ADD KEY `BusId` (`BusId`),
  ADD KEY `BookingId` (`BookingId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserId`),
  ADD UNIQUE KEY `EMail` (`EMail`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `BookingId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bookinghistory`
--
ALTER TABLE `bookinghistory`
  MODIFY `BookingHistoryId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bus`
--
ALTER TABLE `bus`
  MODIFY `BusId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `busfrequency`
--
ALTER TABLE `busfrequency`
  MODIFY `FrequencyId` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `busoperator`
--
ALTER TABLE `busoperator`
  MODIFY `OperatorId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `PaymentId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `seatallocation`
--
ALTER TABLE `seatallocation`
  MODIFY `SeatId` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `UserId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
